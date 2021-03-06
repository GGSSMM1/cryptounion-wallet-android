package io.cryptounion.bankwallet.core

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import io.cryptounion.bankwallet.BuildConfig
import io.cryptounion.bankwallet.core.factories.AccountFactory
import io.cryptounion.bankwallet.core.factories.AdapterFactory
import io.cryptounion.bankwallet.core.factories.AddressParserFactory
import io.cryptounion.bankwallet.core.managers.*
import io.cryptounion.bankwallet.core.notifications.NotificationNetworkWrapper
import io.cryptounion.bankwallet.core.notifications.NotificationWorker
import io.cryptounion.bankwallet.core.providers.AppConfigProvider
import io.cryptounion.bankwallet.core.providers.FeeCoinProvider
import io.cryptounion.bankwallet.core.providers.FeeRateProvider
import io.cryptounion.bankwallet.core.storage.*
import io.cryptounion.bankwallet.modules.keystore.KeyStoreActivity
import io.cryptounion.bankwallet.modules.launcher.LauncherActivity
import io.cryptounion.bankwallet.modules.lockscreen.LockScreenActivity
import io.cryptounion.bankwallet.modules.settings.theme.ThemeType
import io.cryptounion.bankwallet.modules.tor.TorConnectionActivity
import io.cryptounion.bankwallet.modules.walletconnect.WalletConnectManager
import io.cryptounion.bankwallet.modules.walletconnect.WalletConnectRequestManager
import io.cryptounion.bankwallet.modules.walletconnect.WalletConnectSessionManager
import io.horizontalsystems.coinkit.CoinKit
import io.horizontalsystems.core.BackgroundManager
import io.horizontalsystems.core.CoreApp
import io.horizontalsystems.core.ICoreApp
import io.horizontalsystems.core.security.EncryptionManager
import io.horizontalsystems.core.security.KeyStoreManager
import io.horizontalsystems.ethereumkit.core.EthereumKit
import io.horizontalsystems.pin.PinComponent
import io.reactivex.plugins.RxJavaPlugins
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.system.exitProcess

class App : CoreApp() {

    companion object : ICoreApp by CoreApp {

        lateinit var feeRateProvider: FeeRateProvider
        lateinit var localStorage: ILocalStorage
        lateinit var marketStorage: IMarketStorage
        lateinit var torKitManager: ITorManager
        lateinit var chartTypeStorage: IChartTypeStorage
        lateinit var restoreSettingsStorage: IRestoreSettingsStorage

        lateinit var wordsManager: WordsManager
        lateinit var networkManager: INetworkManager
        lateinit var backgroundStateChangeListener: BackgroundStateChangeListener
        lateinit var appConfigProvider: IAppConfigProvider
        lateinit var adapterManager: IAdapterManager
        lateinit var walletManager: IWalletManager
        lateinit var walletStorage: IWalletStorage
        lateinit var accountManager: IAccountManager
        lateinit var accountFactory: IAccountFactory
        lateinit var backupManager: IBackupManager

        lateinit var xRateManager: IRateManager
        lateinit var connectivityManager: ConnectivityManager
        lateinit var appDatabase: AppDatabase
        lateinit var accountsStorage: IAccountsStorage
        lateinit var priceAlertManager: IPriceAlertManager
        lateinit var enabledWalletsStorage: IEnabledWalletStorage
        lateinit var blockchainSettingsStorage: IBlockchainSettingsStorage
        lateinit var ethereumKitManager: EvmKitManager
        lateinit var binanceSmartChainKitManager: EvmKitManager
        lateinit var binanceKitManager: BinanceKitManager
        lateinit var numberFormatter: IAppNumberFormatter
        lateinit var addressParserFactory: AddressParserFactory
        lateinit var feeCoinProvider: FeeCoinProvider
        lateinit var notificationNetworkWrapper: NotificationNetworkWrapper
        lateinit var notificationManager: INotificationManager
        lateinit var ethereumRpcModeSettingsManager: IEthereumRpcModeSettingsManager
        lateinit var initialSyncModeSettingsManager: IInitialSyncModeSettingsManager
        lateinit var derivationSettingsManager: IDerivationSettingsManager
        lateinit var bitcoinCashCoinTypeManager: BitcoinCashCoinTypeManager
        lateinit var accountCleaner: IAccountCleaner
        lateinit var rateAppManager: IRateAppManager
        lateinit var coinManager: ICoinManager
        lateinit var walletConnectSessionStorage: WalletConnectSessionStorage
        lateinit var walletConnectSessionManager: WalletConnectSessionManager
        lateinit var walletConnectRequestManager: WalletConnectRequestManager
        lateinit var walletConnectManager: WalletConnectManager
        lateinit var notificationSubscriptionManager: INotificationSubscriptionManager
        lateinit var termsManager: ITermsManager
        lateinit var marketFavoritesManager: MarketFavoritesManager
        lateinit var coinKit: CoinKit
        lateinit var activateCoinManager: ActivateCoinManager
        lateinit var releaseNotesManager: ReleaseNotesManager
        lateinit var restoreSettingsManager: RestoreSettingsManager
        lateinit var evmNetworkManager: EvmNetworkManager
        lateinit var accountSettingManager: AccountSettingManager
    }

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) {
            //Disable logging for lower levels in Release build
            Logger.getLogger("").level = Level.SEVERE
        }

        RxJavaPlugins.setErrorHandler { e: Throwable? ->
            Log.w("RxJava ErrorHandler", e)
        }

        EthereumKit.init()

        instance = this
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val appConfig = AppConfigProvider()
        appConfigProvider = appConfig
        buildConfigProvider = appConfig
        languageConfigProvider = appConfig

        coinKit = CoinKit.create(this, buildConfigProvider.testMode)

        feeRateProvider = FeeRateProvider(appConfigProvider)
        backgroundManager = BackgroundManager(this)

        appDatabase = AppDatabase.getInstance(this)

        evmNetworkManager = EvmNetworkManager(appConfigProvider)
        accountSettingManager = AccountSettingManager(AccountSettingRecordStorage(appDatabase), evmNetworkManager)

        ethereumKitManager = EvmKitManager(appConfig.etherscanApiKey, backgroundManager, EvmNetworkProviderEth(accountSettingManager))
        binanceSmartChainKitManager = EvmKitManager(appConfig.bscscanApiKey, backgroundManager, EvmNetworkProviderBsc(accountSettingManager))
        binanceKitManager = BinanceKitManager(buildConfigProvider.testMode)

        accountsStorage = AccountsStorage(appDatabase)
        restoreSettingsStorage = RestoreSettingsStorage(appDatabase)

        AppLog.logsDao = appDatabase.logsDao()

        coinManager = CoinManager(coinKit, appConfigProvider)

        enabledWalletsStorage = EnabledWalletsStorage(appDatabase)
        blockchainSettingsStorage = BlockchainSettingsStorage(appDatabase)
        walletStorage = WalletStorage(coinManager, enabledWalletsStorage)

        LocalStorageManager(preferences).apply {
            localStorage = this
            chartTypeStorage = this
            pinStorage = this
            thirdKeyboardStorage = this
            marketStorage = this
        }

        torKitManager = TorManager(instance, localStorage)

        wordsManager = WordsManager()
        networkManager = NetworkManager()
        accountCleaner = AccountCleaner(buildConfigProvider.testMode)
        accountManager = AccountManager(accountsStorage, accountCleaner)
        accountFactory = AccountFactory(accountManager)
        backupManager = BackupManager(accountManager)
        walletManager = WalletManager(accountManager, walletStorage)

        KeyStoreManager("MASTER_KEY", KeyStoreCleaner(localStorage, accountManager, walletManager)).apply {
            keyStoreManager = this
            keyProvider = this
        }

        encryptionManager = EncryptionManager(keyProvider)

        systemInfoManager = SystemInfoManager()

        languageManager = LanguageManager()
        currencyManager = CurrencyManager(localStorage, appConfigProvider)
        numberFormatter = NumberFormatter(languageManager)

        connectivityManager = ConnectivityManager(backgroundManager)

        val zcashBirthdayProvider = ZcashBirthdayProvider(this, buildConfigProvider.testMode)
        restoreSettingsManager = RestoreSettingsManager(restoreSettingsStorage, zcashBirthdayProvider)

        val adapterFactory = AdapterFactory(instance, buildConfigProvider.testMode, ethereumKitManager, binanceSmartChainKitManager, binanceKitManager, backgroundManager, restoreSettingsManager, coinManager)
        adapterManager = AdapterManager(walletManager, adapterFactory, ethereumKitManager, binanceSmartChainKitManager, binanceKitManager)

        initialSyncModeSettingsManager = InitialSyncSettingsManager(coinManager, blockchainSettingsStorage, adapterManager, walletManager)
        derivationSettingsManager = DerivationSettingsManager(blockchainSettingsStorage, adapterManager, walletManager)
        ethereumRpcModeSettingsManager = EthereumRpcModeSettingsManager(blockchainSettingsStorage, adapterManager, walletManager)
        bitcoinCashCoinTypeManager = BitcoinCashCoinTypeManager(walletManager, adapterManager, blockchainSettingsStorage)

        adapterFactory.initialSyncModeSettingsManager = initialSyncModeSettingsManager
        adapterFactory.ethereumRpcModeSettingsManager = ethereumRpcModeSettingsManager

        feeCoinProvider = FeeCoinProvider(coinKit)
        xRateManager = RateManager(this, appConfigProvider)

        addressParserFactory = AddressParserFactory()

        notificationNetworkWrapper = NotificationNetworkWrapper(localStorage, networkManager, appConfigProvider)
        notificationManager = NotificationManager(NotificationManagerCompat.from(this), localStorage).apply {
            backgroundManager.registerListener(this)
        }
        notificationSubscriptionManager = NotificationSubscriptionManager(appDatabase, notificationNetworkWrapper)
        priceAlertManager = PriceAlertManager(appDatabase, notificationSubscriptionManager, notificationManager, localStorage, notificationNetworkWrapper, backgroundManager)

        pinComponent = PinComponent(
                pinStorage = pinStorage,
                encryptionManager = encryptionManager,
                excludedActivityNames = listOf(
                        KeyStoreActivity::class.java.name,
                        LockScreenActivity::class.java.name,
                        LauncherActivity::class.java.name,
                        TorConnectionActivity::class.java.name
                )
        )

        backgroundStateChangeListener = BackgroundStateChangeListener(systemInfoManager, keyStoreManager, pinComponent).apply {
            backgroundManager.registerListener(this)
        }

        rateAppManager = RateAppManager(walletManager, adapterManager, localStorage)
        walletConnectSessionStorage = WalletConnectSessionStorage(appDatabase)
        walletConnectSessionManager = WalletConnectSessionManager(walletConnectSessionStorage, accountManager, accountSettingManager)
        walletConnectRequestManager = WalletConnectRequestManager()
        walletConnectManager = WalletConnectManager(accountManager, ethereumKitManager, binanceSmartChainKitManager)

        termsManager = TermsManager(localStorage)

        marketFavoritesManager = MarketFavoritesManager(appDatabase)

        activateCoinManager = ActivateCoinManager(coinKit, walletManager, accountManager)

        releaseNotesManager = ReleaseNotesManager(systemInfoManager, localStorage, appConfigProvider)

        setAppTheme()

        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks(torKitManager))

        startTasks()

        NotificationWorker.startPeriodicWorker(instance)
    }

    private fun setAppTheme() {
        val nightMode = when (localStorage.currentTheme) {
            ThemeType.Light -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeType.Dark -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeType.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    override fun onTrimMemory(level: Int) {
        when (level) {
            TRIM_MEMORY_BACKGROUND,
            TRIM_MEMORY_MODERATE,
            TRIM_MEMORY_COMPLETE -> {
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */
                if (backgroundManager.inBackground) {
                    val logger = AppLogger("low memory")
                    logger.info("Kill app due to low memory, level: $level")
                    exitProcess(0)
                }
            }
            else -> {  /*do nothing*/
            }
        }
        super.onTrimMemory(level)
    }

    override fun localizedContext(): Context {
        return localeAwareContext(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeAwareContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAwareContext(this)
    }

    private fun startTasks() {
        Thread(Runnable {
            rateAppManager.onAppLaunch()
            accountManager.loadAccounts()
            walletManager.loadWallets()
            adapterManager.preloadAdapters()
            accountManager.clearAccounts()
            notificationSubscriptionManager.processJobs()

            AppVersionManager(systemInfoManager, localStorage).apply { storeAppVersion() }

        }).start()
    }
}
