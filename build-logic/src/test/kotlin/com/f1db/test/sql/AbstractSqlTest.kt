package com.f1db.test.sql

import com.f1db.plugin.writer.sql.Tables.*
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.jooq.DSLContext
import org.jooq.impl.TableImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Stream

/**
 * Abstract class for validating a sql dump file.
 *
 * @author Marcel Overdijk
 */
abstract class AbstractSqlTest {

    companion object {
        @JvmStatic
        fun expectedDatabaseObjectsList(): List<TableImpl<*>> {
            return (getAllTables() + getAllViews())
        }

        @JvmStatic
        fun expectedDatabaseObjects(): Stream<Arguments> {
            return expectedDatabaseObjectsList().map { Arguments.of(it.name, it) }.stream()
        }
    }

    abstract fun getDSLContext(): DSLContext

    @Test
    fun `test database objects exists`() {
        val expectedObjects = expectedDatabaseObjectsList().map { it.name }
        val actualObjects = getDSLContext().meta().tables.map { it.name }
        actualObjects.shouldContainAll(expectedObjects)
    }

    @ParameterizedTest(name = "test {0} has data")
    @MethodSource("expectedDatabaseObjects")
    fun `test database object has data`(tableName: String, obj: TableImpl<*>) {
        val count = getDSLContext()
            .selectCount()
            .from(obj)
            .fetchSingleInto(Int::class.java)
        count.shouldBeGreaterThan(0)
    }

    @Test
    fun `test ayrton-senna data`() {

        // This is a simple test verifying various data in the context of Ayrton Senna.

        // Fetch driver.

        val driver = getDSLContext().selectFrom(DRIVER).where(DRIVER.ID.eq("ayrton-senna")).fetchOne()

        // Verify driver.

        driver.shouldNotBeNull()
        driver.name.shouldBe("Ayrton Senna")
        driver.firstName.shouldBe("Ayrton")
        driver.lastName.shouldBe("Senna")
        driver.fullName.shouldBe("Ayrton Senna da Silva")
        driver.abbreviation.shouldBe("SEN")
        driver.permanentNumber.shouldBeNull()
        driver.gender.shouldBe("MALE")
        driver.dateOfBirth.shouldBe(LocalDate.of(1960, 3, 21))
        driver.dateOfDeath.shouldBe(LocalDate.of(1994, 5, 1))
        driver.placeOfBirth.shouldBe("São Paulo")
        driver.countryOfBirthCountryId.shouldBe("brazil")
        driver.nationalityCountryId.shouldBe("brazil")
        driver.secondNationalityCountryId.shouldBeNull()
        driver.bestChampionshipPosition.shouldBe(1)
        driver.bestRaceResult.shouldBe(1)
        driver.bestStartingGridPosition.shouldBe(1)
        driver.totalChampionshipWins.shouldBe(3)
        driver.totalRaceEntries.shouldBe(162)
        driver.totalRaceStarts.shouldBe(161)
        driver.totalRaceWins.shouldBe(41)
        driver.totalRaceLaps.shouldBe(8219)
        driver.totalPodiums.shouldBe(80)
        driver.totalPoints.stripTrailingZeros().shouldBe(BigDecimal("614.00").stripTrailingZeros())
        driver.totalChampionshipPoints.stripTrailingZeros().shouldBe(BigDecimal("610.00").stripTrailingZeros())
        driver.totalPolePositions.shouldBe(65)
        driver.totalFastestLaps.shouldBe(19)
        driver.totalDriverOfTheDay.shouldBe(0)
        driver.totalGrandSlams.shouldBe(4)

        // Fetch driver family relationships.

        val driverFamilyRelationships = getDSLContext()
            .selectFrom(DRIVER_FAMILY_RELATIONSHIP)
            .where(DRIVER_FAMILY_RELATIONSHIP.DRIVER_ID.eq("ayrton-senna"))
            .fetch()

        // Verify driver family relationships.

        driverFamilyRelationships.shouldHaveSize(1)
        driverFamilyRelationships[0].otherDriverId.shouldBe("bruno-senna")
        driverFamilyRelationships[0].type.shouldBe("SIBLINGS_CHILD")

        // Fetch driver season entries.

        val driverSeasonEntries = getDSLContext()
            .selectFrom(SEASON_ENTRANT_DRIVER)
            .where(SEASON_ENTRANT_DRIVER.DRIVER_ID.eq("ayrton-senna"))
            .fetch()

        // Verify driver season entries.

        driverSeasonEntries.shouldHaveSize(11)
        driverSeasonEntries.map { it.year }.shouldContainAll((1984..1994).toList())

        // Fetch season driver standings.

        val seasonDriverStandings = getDSLContext()
            .selectFrom(SEASON_DRIVER_STANDING)
            .where(SEASON_DRIVER_STANDING.DRIVER_ID.eq("ayrton-senna"))
            .fetch()

        // Verify season driver standings (Championship wins).

        seasonDriverStandings.shouldHaveSize(10)
        seasonDriverStandings.filter { it.positionNumber == 1  }.map { it.year }.shouldContainAll(1988, 1990, 1991)
    }
}
