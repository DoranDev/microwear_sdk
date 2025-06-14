//
//  NJYBleService.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/26.
//1

#import <Foundation/Foundation.h>

#import <CoreBluetooth/CoreBluetooth.h>
#import <NJYBLESDK/NJY_PublicEnum.h>
#import <NJYBLESDK/NJYAsyncCallback.h>
#import <NJYBLESDK/NJY_DailInfoModel.h>
#import <NJYBLESDK/NJY_ContactModel.h>
#import <NJYBLESDK/NJY_AReceiptCodeModel.h>
#import <NJYBLESDK/NJY_TimeSetModel.h>
#import <NJYBLESDK/NJY_AlarmModel.h>
#import <NJYBLESDK/NJY_NotifModel.h>
#import <NJYBLESDK/NJY_AllDaySwitchModel.h>
#import <NJYBLESDK/NJY_FemaleHealthModel.h>
#import <NJYBLESDK/NJY_WeatherModel.h>
#import <NJYBLESDK/NJY_UserInfoModel.h>
#import <NJYBLESDK/NJY_Peripherial.h>
#import <NJYBLESDK/NJY_SysDataModel.h>
#import <NJYBLESDK/NJY_SleepModel.h>
#import <NJYBLESDK/NJY_SportRecordModel.h>
#import <NJYBLESDK/NJY_EcgModel.h>
#import <NJYBLESDK/NJY_DeviceConfigModel.h>
#import <NJYBLESDK/NJY_DeviceFunModel.h>
#import <NJYBLESDK/NJY_TypeMedicModel.h>
#import <NJYBLESDK/NJY_GameModel.h>
#import <NJYBLESDK/NJY_SportStateModel.h>
#import <NJYBLESDK/NJY_WeatherForecastModel.h>
#import <NJYBLESDK/NJY_CurDateTimeModel.h>
#import <NJYBLESDK/NJY_MotionGameModel.h>
#import <NJYBLESDK/NJY_DocumentFileModel.h>
#import <NJYBLESDK/NJY_NaviModel.h>
#import <NJYBLESDK/NJY_QiblaModel.h>
#import <NJYBLESDK/NJY_Mindful.h>
#import <NJYBLESDK/NJY_SharesModel.h>
#import <NJYBLESDK/NJY_NewModel.h>
#import <NJYBLESDK/NJY_SchedDuleModel.h>
#import <NJYBLESDK/NJY_GPSInfoModel.h>
#import <NJYBLESDK/NJY_WxModel.h>
#import <NJYBLESDK/PA_SmokeRecordModel.h>
#import <NJYBLESDK/PA_SmokeTypePower.h>

#define KSCANPERIPHERA_NOTIF                             @"scan_peripheral"                                 //扫描到设备
#define KPERIPHERIALCONNECTED_NOTIF                      @"peripherial_connected"                           //连接上设备
#define KBLEDISCONNECTED_NOTIF                           @"bleDisconnected"                                 //设备断开
#define KBLECONNECTED_NOTIF                              @"NotificationBleConnected"                        //设备连接
#define KBLEOPENTAKEPHOTO_NOTIF                          @"NotificationOpenTtakePhoto"                      //拍照打开
#define KBLEGPSSPORT_NOTIF                               @"NotificationGpsSport"
#define KBLENAVI_NOTIF                                   @"NotificationNavi"                                //停止导航
#define KBLEQIBLANOTIF                                   @"NotificationQibla"                                //朝拜
#define KBLMINDFULNOTIF                                  @"NotificationMINDFUL"                                //心情状态
#define KBLESTOCKNOTIF                                   @"NotificationSTOCK"                                //股票
#define KBLEOTADONENOTIF                                 @"NotificationOTADONE"                                //OTA主动结束
#define NJYFLT_BLEBlePower                               @"NJYFLT_BLEBlePower"                                //蓝牙状态
#define kBLENEWLIST                                       @"NotificationNewList"                                //BLE请求新问

//GPS运动
#define KBLEGPSSPORTSTOP_NOTIF                           @"NotificationGpsSportStop"                        //GPS运动暂停
#define KBLEGPSSPORTCONTINUETO_NOTIF                     @"NotificationGpsSportcontinueTo"                  //GPS运动继续
#define KBLEGPSSPORTEnd_NOTIF                            @"NotificationGpsSportEnd"                         //GPS运动结束
#define KBLEGPSSPORT_NOTIF_COORDINATE                            @"NotificationGpsSportcoordinate"                         //设备GPS运动结束坐标
#define KBLEBAT_NOTIF                                    @"NotificationBLEBAT_NOTIF"                         //电量提醒
#define KBLEOPUSFILEDONE_NOTIF                           @"NotificationBLEOpusDone_NOTIF"                         //获取pcm文件
#define KBLADDRESS_NOTIF                                 @"NotificationBLEADDRESS_NOTIF"                         //设置地址
#define KLANGTrANSLATANOTIF                              @"LANGTrANSLATANOTIF"                         //翻译英语
#define KLANGIDANOTIF                                    @"LANGIDANOTIF"                         //识别语言
#define KLANGTYPEANOTIF                                  @"LANGTYPEANOTIF"                         //语音功能
#define KISBLEGPSSPORT_NOTIF                            @"NotificationisGpsSport"                         //是否开启GPS运动
#define KBLEWXState_NOTIF                               @"NotificationWxState"                      //微信状态通知

#define KBLEWXDEVICEState_NOTIF                               @"NotificationWxDeviceState"                      //微信设备状态通知

#define KBLEWHState_NOTIF                               @"NotificationWHState"                      //whats状态通知

#define KBLEWHDEVICEState_NOTIF                               @"NotificationWHDeviceState"                      //whats设备状态通知


#define kXYFileSavePathSDK            @"XYFileSavePath" //星历
#define KBLESMOKEPOWERNOTIF                              @"NotificationSMOKEPOWER"                                //吸烟功率

@protocol NJYBleServiceDelegate <NSObject>

@optional

/// Description
/// @param state
///  CBManagerStateUnknown = 0,
///  CBManagerStateResetting,
///  CBManagerStateUnsupported,
///  CBManagerStateUnauthorized,
///  CBManagerStatePoweredOff,
///  CBManagerStatePoweredOn,
/// @param des des description
- (void)centralManagerDidUpdateState:(NSInteger)state des:(NSString *_Nullable)des;

@end

@class NJY_Peripherial;

NS_ASSUME_NONNULL_BEGIN
@interface NJYBleService : NSObject

@property (nonatomic, weak) id<NJYBleServiceDelegate> delegate;
@property (nonatomic, strong) NSMutableArray *bleModals;            //设备ModelList
@property (nonatomic, strong) CBPeripheral *myPeripherial;     //连接设备
@property (nonatomic, assign) BOOL isBinded;                      //是否连接过
@property (nonatomic, assign) NSInteger batteryLevel;              //电量
@property (nonatomic, assign) BOOL isPostConnected;
@property (nonatomic, assign) BOOL isSystem_Type;
@property (nonatomic, assign) BOOL isNewConnected;
@property (nonatomic, strong) NSArray<NJY_GameModel*>* gameList;
@property (nonatomic, assign) CGSize dailSize;                     //表盘大小
@property (nonatomic, assign) NSInteger sessionStatus;              //0:开启新会话 1:当前会话

+ (instancetype)sharedInstance;

/// 测试
-(NSString *)testString;

/// 扫描
- (void)scan;

/// 停止扫描
- (void)stopScan;


/// 连接BLE
/// @param peripherial peripherial description
- (void)connect:(CBPeripheral *)peripherial;

/// 重连设备
- (void)reconnect;

/// 设备断连
- (void)unbind;

- (void)unOTAOK;

/// 获取当前绑定的外设
/// @param uuidString  uuidString
- (CBPeripheral *)retrievePeripheralWith:(NSString *)uuidString;

/// 写入指令
/// @param type 功能ID
/// @param value 数据参数
//- (void)sendCommand:(NJY_CommandType)type data:(id _Nullable)value;

#pragma mark - ****************发送设置
/**
 *
 *指令函数-(void)send;
 */

/// 查找手环 EVT_TYPE_ALERT_FIND_WATCH
-(void)sendAlertFindWatch;

/// 拍照 EVT_TYPE_TAKE_PHOTO NSInteger isVideoDial = [[kUserDefaults objectForKey:AARuiyuCustomVideoDial]integerValue
/// @param isOpen 状态 0=打开；0=关闭
-(void)sendTakePhoto:(NSInteger)isOpen;

/// 抬腕亮屏 EVT_TYPE_RAISE_WRIST
/// @param model 只需要设置model isOn
-(void)sendRaiseWrist:(NJY_TimeSetModel *) model;

/// 久坐提醒 EVT_TYPE_LONG_SIT
/// @param model model description
-(void)sendLongSit:(NJY_TimeSetModel *) model;

/// 喝水提醒 EVT_TYPE_DRINK_WATER
/// @param model model description
-(void)sendDrinkWater:(NJY_TimeSetModel *) model;

///注意一次要设置4次；如果空白闹钟值为0
/// 设置闹钟  EVT_TYPE_ALARM
/// @param model model description
-(void)sendAlarm:(NJY_AlarmModel *) model;

/// 设置时间格式 EVT_TYPE_TIME_MODE
/// @param note  0:24h制，1：12h制
-(void)sendTimeMode:(NSInteger) note;

/// 设置温度格式 EVT_TYPE_TEMP_UNIT
/// @param note 0:摄氏度制，1：华氏度制（手表默认摄氏度制）
-(void)sendTempUnit:(NSInteger) note;

/// 设置单位制度 EVT_TYPE_UNIT_SYSTEM
/// @param note 0:公制，1：英制（手表默认公制）
-(void)sendUnitSystem:(NSInteger) note;

/// 设置消息通知 EVT_TYPE_NOTIFICATIONS
/// @param model model description
-(void)sendNotif:(NJY_NotifModel *)model;

/// 设置全天测量开关 EVT_TYPE_ALL_DAY_FALG
/// @param model model description
-(void)sendAllDayFalg:(NJY_AllDaySwitchModel *) model;

/// 女性健康 EVT_TYPE_WOMEN_HEALTH
/// @param model model description
-(void)sendWomenHealth:(NJY_FemaleHealthModel *) model;

/// 设置语言 EVT_TYPE_LANGUAGE
/// @param language language description
-(void)sendLanguage:(NJY_Language) language;

/// 设置日期、时间  EVT_TYPE_DATE_TIME
/// @param   model
///
-(void)sendCurDateTime:(NJY_CurDateTimeModel*)model;

/// 设置Ai语音
/// @param type type description
-(void)sendRecording:(NSInteger)type;
/// 实时天气 EVT_TYPE_REAL_TIME_WEATHER
/// @param model model description
-(void)sendRealTimeWeather:(NJY_WeatherModel *) model;

/// 设置天气预报
/// @param model model description
-(void)sendWeatherForecast:(NJY_WeatherForecastModel *) model;

/// 目标步数 EVT_TYPE_TARGET_STEP
/// @param size 目标步数
-(void)sendTargetStep:(NSInteger)size;

/// 用户信息 EVT_TYPE_USER_INFO
/// @param model model description
-(void)sendUserInfo:(NJY_UserInfoModel*) model;

/// 设置地址
/// @param address 地址
-(void)sendAddress:(NSString*)address;
/// 设置海拔 EVT_TYPE_ALTITUDE
/// @param 海拔
-(void)sendUserAltitude:(NSInteger) altitude;

/// 删除文件
-(void)sendDeleteSdFile:(NJY_DocumentFileModel*)file;

/// 设置用户名称（仅支持杰里二代)
-(void)sendUserName:(NSString*)Name;

/// 设置导航
/// - Parameter naviModel: naviModel description
-(void)sendNaviMap:(NJY_NaviModel*)naviModel;

/// 发送手机系统
-(void)sendPhoneSystem;

/// 发送AI信息
/// - Parameter var: 发送AI信息内容
-(void)sendAIValue:(NSString *)var;

-(void)sendVoiceValue:(NSString *)var;
/// 移动设备蓝牙开关状态
- (BOOL)getCentralManagerStatus;

#pragma mark - **************** callback函数有响应返回
/*函数有返回值
 */
/// 固件OTA升级
/// @param peripheral 设备peripheral对象
/// @param otaType otaType = 0 普通升级；otaType = 1 静默升级
/// @param path 升级包路径
/// @param callback callback  result 1
- (void)sendDeviceOta:(CBPeripheral *)peripheral otaType:(EVT_OTA_TYPE)otaType path:(NSString*)path funId:(NSInteger)funid callback:(NJYAsyncCallback *)callback;

/// 系统表盘
/// @param path 表盘路径
/// @param data data nil
/// @param type type 1
/// @param callback callback  result       OTA_TYPE_SUCCESS            = 0,//升级成功
- (void)sendDialInstall:(NSString *)path data:(NSData*)data type:(NSInteger)type callback:(NJYAsyncCallback *)callback;

/// 自定义表盘
/// @param dailInfo 表盘信息Model
/// @param type 0
/// @param callback callback   result       OTA_TYPE_SUCCESS            = 0,//升级成功
- (void)sendCustomDialInstall:(NJY_DailInfoModel *)dailInfo type:(NSInteger)type callback:(NJYAsyncCallback *)callback;

/// 电子书音乐OTA
/// @param path 文件URL
/// @param data NSData
/// @param fileName 名称
/// @param type 类型 50: 音乐  51:电子
/// @param callback callback  result       OTA_TYPE_SUCCESS            = 0,//升级成功
- (void)sendFileInstall:(NSString*)path data:(NSData*)data fileName:(NSString*)fileName type:(NSInteger)type callback:(NJYAsyncCallback *)callback;

/// 删除OTAclearCallBack
-(void)clearOTACallBack;

/// 拍照 旧函数sendRuiyuInstruction
/// @param value true false
/// @param callback callback  result  0 = /* 关闭 */;1 =/* 拍照 */ ; 2=/* 打开 */
- (void)sendTakePhoto:(id)value callback:(NJYAsyncCallback *)callback;

/// 联系人
/// @param contactList NJY_ContactModel list
/// @param callback callback  result       OTA_TYPE_SUCCESS            = 0;成功
- (void)sendContact:(NSArray<NJY_ContactModel *>*)contactList callback:(NJYAsyncCallback *)callback;

/// 收藏联系人
/// @param contactList NJY_ContactModel list
/// @param callback callback  result       OTA_TYPE_SUCCESS            = 0;成功
- (void)sendCollectContact:(NSArray<NJY_ContactModel *>*)contactList callback:(NJYAsyncCallback *)callback;

/// 同步数据
/// @param callback callback result NJY_SysDataModel
- (void)sendSysData:(NJYAsyncCallback *)callback;
/// 实时运动
/// @param callback callback description
-(void)sendSysSportData:(NJYAsyncCallback *)callback;
//实时监听心率
-(void)sendSysHRData:(NJYAsyncCallback *)callback;
//实时监听血压
-(void)sendSysBPData:(NJYAsyncCallback *)callback;
//实时监听血氧
-(void)sendSysBOData:(NJYAsyncCallback *)callback;
/// 获取SD卡剩余空间
/// @param data 返回剩余空间（KB)
-(void)sysFreeSpaceData:(NSData*)data;
/// 睡眠数据
/// @param callback callback result NJY_SleepModel
- (void)sendSleepData:(NJYAsyncCallback *)callback;

/// 运动记录
/// @param callback callback result NJY_SportRecordModel
- (void)sendTrainingData:(NJYAsyncCallback *)callback;

/// 闹钟
/// @param callback callback result list<NJY_AlarmModel*>
-(void) getAlarmData:(NJYAsyncCallback *)callback;

/// 同步24小时步数
/// @param callback callback list<NJY_SysDataModel>
- (void)sendSysHourData:(NJYAsyncCallback *)callback;
/// 同步24小时Kcal
/// @param callback callback result list<NJY_SysDataModel>
- (void)sendSysHourKcalData:(NJYAsyncCallback *)callback;

/// 联系人 付款码
/// @param model 付款Model
/// @param callback callback  result = 0,成功
-(void)sendFriendeceiptCode:(NJY_AReceiptCodeModel*)model callback:(NJYAsyncCallback *)callback;

/// 同步24小时心率
/// @param callback callback result list<NJY_SysDataModel*> //30分钟一次
- (void)sendSysHourHeartrateData:(NJYAsyncCallback *)callback;

/// 同步24小时血压
/// @param callback callback result list<NJY_SysDataModel*> //30分钟一次
- (void)sendSysHourBloodData:(NJYAsyncCallback *)callback;

/// 同步24小时血氧
/// @param callback callback result list<NJY_SysDataModel*> //30分钟一次
- (void)sendSysHourBloodRaiseData:(NJYAsyncCallback *)callback;

/// 同步消息通知
/// @param callback callback result NJY_NotifModel
-(void) getNotifcations:(NJYAsyncCallback *)callback;

/// 读取适配号
/// @param callback callback result result NSString
-(void)getBindedDeviceInfo:(NJYAsyncCallback *)callback;

/// 开启心电
/// @param type ：1：开始 0：停止
/// @param callback callback result NJY_EcgModel
-(void)getDeviceEcg:(NSInteger)type callback:(NJYAsyncCallback *)callback;

/// 获取版本
/// @param callback callback result 字符串版本
-(void)getDeviceVer:(NJYAsyncCallback *)callback;

/// 获取设备信息
/// @param callback callback result NJY_DeviceConfigModel
-(void)getDeviceConfig:(NJYAsyncCallback *)callback;

/// 获取电量
/// @param callback callback result NSInteger
-(void) getDeviceBat:(NJYAsyncCallback *)callback;

/// BLE蓝牙状态
/// @param callback callback description
-(void) getCentralManagerDidUpdateState:(NJYAsyncCallback *)callback;

/// 获取亮屏状态
/// @param callback callback description
-(void) getDisplayInfo:(NJYAsyncCallback *)callback;

/// 获取固件信息
/// @param callback NJY_DeviceFunModel
-(void) getDeviceFun:(NJYAsyncCallback *)callback;

/// 设置吃药提醒
/// @param model model description
-(void)sendTypeMedic:(NJY_TypeMedicModel*)model;

/// 获取吃药提醒
/// @param callback NJY_TypeMedicModel
-(void)getTypeMedic:(NJYAsyncCallback *)callback;

/// 获取久坐提醒
/// @param callback callback description
-(void) getLongSit:(NJYAsyncCallback *)callback;

/// 喝水提醒 EVT_TYPE_DRINK_WATER
/// @param callback callback description
-(void) getDrinkWater:(NJYAsyncCallback *)callback;

/// GPS状态
/// @param model model description
-(void)getGPSStart:(NJY_SportStateModel*)model;

/// GPS运动功能
/// @param model model description
/// @param callback callback description
-(void)getGPSSynData:(NJY_SportStateModel*)model callback:(NJYAsyncCallback *)callback;

/// GPS运动OTA
/// @param model model description
/// @param gpsImageData gpsImageData description
/// @param callback callback description
-(void)sendGpsSportOta:(NJY_SportStateModel*)model gpsImageData:(NSData*)gpsImageData callback:(NJYAsyncCallback *)callback;

/// 体感游戏
/// @param Type 0：游戏开始        1：游戏结束    2：游戏暂停    3：传输游戏数据
/// @param callback NJY_MotionGameModel description  当 Type = 2 callBack未nil
-(void)sendMotionGame:(NSInteger)type callback:(NJYAsyncCallback *)callback;

/// 获取SD歌曲名称列表（仅支持杰里二代)
/// /// @param callBack callBack NJY_DocumentFileModel
-(void)sendMusicList:(NJYAsyncCallback *)callBack;

/// 获取SD电子书名称列表
/// @param callBack callBack NJY_DocumentFileModel
-(void)sendSdBookList:(NJYAsyncCallback *)callBack;

/// 获取SD录音
/// @param callBack NJY_DocumentFileModel
-(void)sendSdRecordList:(NJYAsyncCallback *)callBack;

/// 同步录屏文件
/// @param model 文件model
/// @param callBack callBack 文件NSDATA
-(void)sendSdAcceptFiled:(NJY_DocumentFileModel*)model callBack:(NJYAsyncCallback *)callBack;

/// 获取APP图片key
/// @param callBack Key
-(void)senduserImage:(NJYAsyncCallback *)callBack;

/// BLE3.0
/// @param callback callback description
- (void)sendSysBle3:(NJYAsyncCallback *)callback;

/// 朝拜
/// @param qiblaModel NJY_QiblaModel 朝拜Model
- (void)sendQibla:(NJY_QiblaModel *)qiblaModel;


/// 同步股票信息
/// @param model 股票model
-(void)sendSharesSynData:(NJY_SharesModel*)model;

/// 修改BUG名称
/// @param name name description
-(void)sendBleName:(NSString*)name;
-(void)disconnectPeripheral:(CBPeripheral *)peripheral;

/// 发送新闻
/// @param newList 新闻列表
-(void)sendNewList:(NSArray<NSString*>*)newList;
/// 发送新闻详情
/// @param str 新闻详情
/// @param index 当前新闻index
-(void)sendNewInfo:(NSString*)str newsIndex:(NSInteger)index;
/// 日程管理
/// @param list 数据类List
-(void)sendScheDuleInfoList:(NSArray<NJY_SchedDuleModel*>*)list;

/// 发送二维码
/// @param mode 二维码地址
-(void)sendWxGetQRx:(NJY_WxModel*)mode;

/// whatsAPP发送二维码
/// @param mode 二维码地址
-(void)sendWHGetQRx:(NJY_WxModel*)mode;
/// 当天吸烟历史数据
/// @param callback callback result PA_SmokeRecordModel
//- (void)sendSysSmokeData:(NJYAsyncCallback *)callback;

/// 一天吸烟时间段数据
/// @param callback callback result list<PA_SmokeRecordModel*>
-(void)sendSysDay:(NSInteger)index SmokeData:(NJYAsyncCallback *)callback;

/// 设置电子烟功率数据
/// @param model 功率model
-(void)sendTypePower:(PA_SmokeTypePower *) model;

/// 同步电子烟功率数据
// @param callback callback result PA_SmokeTypePower
-(void) getTypePower;///:(NJYAsyncCallback *)callback;

@end
NS_ASSUME_NONNULL_END

