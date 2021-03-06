/* Copyright (C) 2018 Michael Comella
 *
 * This file is part of NonContactCallBlocker.
 *
 *  NonContactCallBlocker is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  NonContactCallBlocker is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with NonContactCallBlocker.  If not, see
 *  <https://www.gnu.org/licenses/>. */

package xyz.mcomella.noncontactcallblocker.repository

import androidx.lifecycle.LiveData
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import xyz.mcomella.noncontactcallblocker.db.BlockedCallDao
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
import java.util.Date

class BlockedCallRepositoryTest {

    private lateinit var repository: BlockedCallRepository
    @MockK(relaxed = true) private lateinit var dao: BlockedCallDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = BlockedCallRepository(dao)
    }

    @Test // A better test would test the inputs/outputs are the same but this is good enough for now.
    fun `WHEN loading blocked calls THEN the value directly from the DB is returned`() {
        val expected: LiveData<List<BlockedCallEntity>> = mockk()
        every { dao.loadBlockedCalls() } returns expected
        assertEquals(expected, repository.getBlockedCalls())
    }

    @Test // A better test would test the input/outputs.
    fun `WHEN a repository receives a blocked call THEN the DAO tries to insert it`() = runBlocking {
        val expectedNumber = "55555555555"
        val expectedDate = Date(1234567)
        val expectedEntity = BlockedCallEntity(expectedNumber, expectedDate)

        repository.onCallBlocked(expectedNumber, expectedDate)
        verify { dao.insertBlockedCalls(expectedEntity) }
    }
}
