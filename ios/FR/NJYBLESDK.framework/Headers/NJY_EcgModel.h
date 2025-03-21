//
//  NJY_EcgModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/19.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_EcgModel : NSObject
@property (nonatomic, assign) NSInteger ecgSize;//心电数值
@property (nonatomic, assign) NSInteger ecgType;// type ：0 : 测量中 1:未佩戴  2: 测量完成结果 3.固件主动停止测量

@end

NS_ASSUME_NONNULL_END
