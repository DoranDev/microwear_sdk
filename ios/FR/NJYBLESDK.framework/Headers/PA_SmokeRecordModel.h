//
//  PA_SmokeRecordModel.h
//  NJYBLESDK
//
//  Created by dino on 2024/11/11.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface PA_SmokeRecordModel : NSObject

@property (nonatomic, assign) NSInteger timeStamp;           // 时间戳
@property (nonatomic, assign) NSInteger mouth;               // 口数
@property (nonatomic, assign) NSInteger time_len;            // 时长，毫秒
//@property (nonatomic, assign) NSInteger currtime_len;        // 当前口时长，毫秒

@end

NS_ASSUME_NONNULL_END
