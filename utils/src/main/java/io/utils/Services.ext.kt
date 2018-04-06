package io.utils

import android.accounts.AccountManager
import android.app.*
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.usage.NetworkStatsManager
import android.app.usage.StorageStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.companion.CompanionDeviceManager
import android.content.ClipboardManager
import android.content.Context
import android.content.RestrictionsManager
import android.content.pm.LauncherApps
import android.content.pm.ShortcutManager
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.fingerprint.FingerprintManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.aware.WifiAwareManager
import android.net.wifi.p2p.WifiP2pManager
import android.nfc.NfcManager
import android.os.*
import android.os.health.SystemHealthManager
import android.os.storage.StorageManager
import android.print.PrintManager
import android.support.annotation.RequiresApi
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textclassifier.TextClassificationManager
import android.view.textservice.TextServicesManager

val usbManager: UsbManager get() = baseContext.getSystemService(Context.USB_SERVICE) as UsbManager
val accessibilityManager: AccessibilityManager get() = baseContext.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
val accountManager: AccountManager get() = baseContext.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
val activityManager: ActivityManager get() = baseContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
val alarmManager: AlarmManager get() = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
val appWidgetManager: AppWidgetManager get() = AppWidgetManager.getInstance(baseContext)
val audioManager: AudioManager get() = baseContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
@get:RequiresApi(Build.VERSION_CODES.KITKAT)
val appOpsManager: AppOpsManager
    get() = baseContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val batteryManager: BatteryManager
    get() = baseContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
@get:RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
val bluetoothManager: BluetoothManager
    get() = baseContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val cameraManager: CameraManager
    get() = baseContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
@get:RequiresApi(Build.VERSION_CODES.KITKAT)
val captioningManager: CaptioningManager
    get() = baseContext.getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager
@get:RequiresApi(Build.VERSION_CODES.M)
val carrierConfigManager: CarrierConfigManager
    get() = baseContext.getSystemService(Context.CARRIER_CONFIG_SERVICE) as CarrierConfigManager
val clipboardManager: ClipboardManager get() = baseContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
@get:RequiresApi(Build.VERSION_CODES.O)
val companionDeviceManager: CompanionDeviceManager
    get() = baseContext.getSystemService(Context.COMPANION_DEVICE_SERVICE) as CompanionDeviceManager
val connectivityManager: ConnectivityManager get() = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
@get:RequiresApi(Build.VERSION_CODES.KITKAT)
val consumerIrManager: ConsumerIrManager
    get() = baseContext.getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager
val devicePolicyManager: DevicePolicyManager get() = baseContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
val displayManager: DisplayManager get() = baseContext.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
val downloadManager: DownloadManager get() = baseContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
val dropBoxManager: DropBoxManager get() = baseContext.getSystemService(Context.DROPBOX_SERVICE) as DropBoxManager
@get:RequiresApi(Build.VERSION_CODES.M)
val fingerprintManager: FingerprintManager
    get() = baseContext.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
@get:RequiresApi(Build.VERSION_CODES.N)
val hardwarePropertiesManager: HardwarePropertiesManager
    get() = baseContext.getSystemService(Context.HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager
val inputMethodManager: InputMethodManager get() = baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
val inputManager: InputManager get() = baseContext.getSystemService(Context.INPUT_SERVICE) as InputManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val jobSchedulerManager: JobScheduler
    get() = baseContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
val keyguardManager: KeyguardManager get() = baseContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val launcherApps: LauncherApps
    get() = baseContext.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
val layoutInflater: LayoutInflater get() = LayoutInflater.from(baseContext)
val locationManager: LocationManager get() = baseContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val mediaProjectionManager: MediaProjectionManager
    get() = baseContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
val mediaRouter: MediaRouter get() = baseContext.getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val mediaSessionManager: MediaSessionManager
    get() = baseContext.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
@get:RequiresApi(Build.VERSION_CODES.M)
val midiManager: MidiManager
    get() = baseContext.getSystemService(Context.MIDI_SERVICE) as MidiManager
@get:RequiresApi(Build.VERSION_CODES.M)
val networkStatusManager: NetworkStatsManager
    get() = baseContext.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
val nfcManager: NfcManager get() = baseContext.getSystemService(Context.NFC_SERVICE) as NfcManager
val notificationManager: NotificationManager get() = baseContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
val nsdManager: NsdManager get() = baseContext.getSystemService(Context.NSD_SERVICE) as NsdManager
val powerManager: PowerManager get() = baseContext.getSystemService(Context.POWER_SERVICE) as PowerManager
@get:RequiresApi(Build.VERSION_CODES.KITKAT)
val printManager: PrintManager
    get() = baseContext.getSystemService(Context.PRINT_SERVICE) as PrintManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val restrictionsManager: RestrictionsManager
    get() = baseContext.getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager
val searchManager: SearchManager get() = baseContext.getSystemService(Context.SEARCH_SERVICE) as SearchManager
val sensorManager: SensorManager get() = baseContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
@get:RequiresApi(Build.VERSION_CODES.N_MR1)
val shortcutManager: ShortcutManager
    get() = baseContext.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
val storageManager: StorageManager get() = baseContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
@get:RequiresApi(Build.VERSION_CODES.O)
val storageStatsManager: StorageStatsManager
    get() = baseContext.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
@get:RequiresApi(Build.VERSION_CODES.N)
val systemHealthManager: SystemHealthManager
    get() = baseContext.getSystemService(Context.SYSTEM_HEALTH_SERVICE) as SystemHealthManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val telecomManager: TelecomManager
    get() = baseContext.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
val telephonyManager: TelephonyManager get() = baseContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
val telephonySubscriptionManager: SubscriptionManager
    get() = baseContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
@get:RequiresApi(Build.VERSION_CODES.O)
val textClassificationManager: TextClassificationManager
    get() = baseContext.getSystemService(Context.TEXT_CLASSIFICATION_SERVICE) as TextClassificationManager
val textServicesManager: TextServicesManager get() = baseContext.getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE) as TextServicesManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP)
val tvInputManager: TvInputManager
    get() = baseContext.getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager
val uiModeManager: UiModeManager get() = baseContext.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
@get:RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
val usageStatsManager: UsageStatsManager
    get() = baseContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
val userManager: UserManager get() = baseContext.getSystemService(Context.USER_SERVICE) as UserManager
val vibratorManager: Vibrator get() = baseContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
val wallpaperManager: WallpaperManager get() = baseContext.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
@get:RequiresApi(Build.VERSION_CODES.O)
val wifiAwareManager: WifiAwareManager
    get() = baseContext.getSystemService(Context.WIFI_AWARE_SERVICE) as WifiAwareManager
val wifiP2pManager: WifiP2pManager get() = baseContext.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
val wifiManager: WifiManager get() = baseContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
val windowManager: WindowManager get() = baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager