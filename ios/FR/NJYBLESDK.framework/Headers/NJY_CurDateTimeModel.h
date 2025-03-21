//
//  NJY_CurDateTimeModel.h
//  NJYBLESDK
//
//  Created by Michael on 2023/1/4.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_CurDateTimeModel : NSObject
@property (nonatomic, assign) NSInteger curInterval;                              // 当前时间戳
@property (nonatomic, assign) NSInteger curTimeZone;                              // 时区1
@property (nonatomic, assign) NSInteger curTimeZone2;                              // 时区2

@end

NS_ASSUME_NONNULL_END
