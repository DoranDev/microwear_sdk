//
//  NJY_LogManager.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>
NS_ASSUME_NONNULL_BEGIN

//#if DEBUG
//#define saveLog(format, ...) NSLog(format);
//
//#else
#define saveLog(format, ...) [[NJY_LogManager sharedInstance] logInfo:__func__ line:__LINE__ logStr:[NSString stringWithFormat:format, ## __VA_ARGS__]];
//#endif



#define GTcLocal(var) NSLocalizedString(var, nil)



@interface NJY_LogManager : NSObject

/**
 *  获取单例实例
 *
 *  @return 单例实例
 */
+ (instancetype) sharedInstance;

#pragma mark - Method
/**
 写入日志
 
 @param funcName 方法名称
 @param line 行数
 @param logStr 日志消息
 */
- (void)logInfo:(const char *)funcName line:(int)line logStr:(NSString*)logStr;

/**
 *  清空过期的日志
 */
- (void)clearExpiredLog;
- (NSArray *)getLogFiles;
- (NSString*)getLogFilePath;
@end

NS_ASSUME_NONNULL_END
