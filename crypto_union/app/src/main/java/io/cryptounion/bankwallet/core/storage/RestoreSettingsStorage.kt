package io.cryptounion.bankwallet.core.storage

import io.cryptounion.bankwallet.core.IRestoreSettingsStorage
import io.cryptounion.bankwallet.entities.RestoreSettingRecord

class RestoreSettingsStorage(appDatabase: AppDatabase) : IRestoreSettingsStorage {
    private val dao: RestoreSettingDao by lazy {
        appDatabase.restoreSettingDao()
    }

    override fun restoreSettings(accountId: String, coinId: String): List<RestoreSettingRecord> {
        return dao.get(accountId, coinId)
    }

    override fun restoreSettings(accountId: String): List<RestoreSettingRecord> {
        return dao.get(accountId)
    }

    override fun save(restoreSettingRecords: List<RestoreSettingRecord>) {
        dao.insert(restoreSettingRecords)
    }

    override fun deleteAllRestoreSettings(accountId: String) {
        dao.delete(accountId)
    }
}
