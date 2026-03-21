"""SQLite database connection manager."""

import sqlite3
from pathlib import Path
from contextlib import contextmanager

DB_PATH = Path(__file__).parent / "f1db.sqlite"


def get_connection() -> sqlite3.Connection:
    """Get a read-only SQLite connection with row factory."""
    conn = sqlite3.connect(f"file:{DB_PATH}?mode=ro", uri=True)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys=ON")
    return conn


@contextmanager
def get_db():
    """Context manager for database connections."""
    conn = get_connection()
    try:
        yield conn
    finally:
        conn.close()


def query_all(sql: str, params: tuple = ()) -> list[dict]:
    """Execute a query and return all rows as dicts."""
    with get_db() as conn:
        cursor = conn.execute(sql, params)
        return [dict(row) for row in cursor.fetchall()]


def query_one(sql: str, params: tuple = ()) -> dict | None:
    """Execute a query and return a single row as dict."""
    with get_db() as conn:
        cursor = conn.execute(sql, params)
        row = cursor.fetchone()
        return dict(row) if row else None


def query_count(sql: str, params: tuple = ()) -> int:
    """Execute a COUNT query and return the count."""
    with get_db() as conn:
        cursor = conn.execute(sql, params)
        return cursor.fetchone()[0]


def query_paginated(sql: str, count_sql: str, params: tuple = (),
                    limit: int = 50, offset: int = 0) -> tuple[list[dict], int]:
    """Execute a paginated query, returning (rows, total_count)."""
    with get_db() as conn:
        cursor = conn.execute(count_sql, params)
        total = cursor.fetchone()[0]

        cursor = conn.execute(f"{sql} LIMIT ? OFFSET ?", params + (limit, offset))
        rows = [dict(row) for row in cursor.fetchall()]

        return rows, total
