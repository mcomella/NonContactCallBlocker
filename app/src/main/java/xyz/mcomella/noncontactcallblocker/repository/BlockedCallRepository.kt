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
import androidx.annotation.AnyThread
import kotlinx.coroutines.experimental.launch
import xyz.mcomella.noncontactcallblocker.db.AppDB.Companion.dbDispatcher
import xyz.mcomella.noncontactcallblocker.db.BlockedCallDao
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
import java.util.Date

class BlockedCallRepository(
    private val blockedCallDao: BlockedCallDao
) {

    fun getBlockedCalls(): LiveData<List<BlockedCallEntity>> {
        return blockedCallDao.loadBlockedCalls()
    }

    @AnyThread
    fun onCallBlocked(number: String?, date: Date) {
        val entity = BlockedCallEntity(number, date)
        launch(dbDispatcher) {
            blockedCallDao.insertBlockedCalls(entity)
        }
    }
}
