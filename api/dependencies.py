"""Common FastAPI dependencies for pagination and sorting."""

from fastapi import Query


class PaginationParams:
    def __init__(
        self,
        limit: int = Query(default=50, ge=1, le=200, description="Number of results to return"),
        offset: int = Query(default=0, ge=0, description="Number of results to skip"),
    ):
        self.limit = limit
        self.offset = offset
