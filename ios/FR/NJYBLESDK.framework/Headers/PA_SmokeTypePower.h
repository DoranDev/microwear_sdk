//
//  PA_SmokeTypePower.h
//  NJYBLESDK
//
//  Created by dino on 2024/12/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface PA_SmokeTypePower : NSObject

@property (nonatomic, assign) NSInteger mode;   //模式 0--power模式，1--smart模式，2--高中低模式
@property (nonatomic, assign) NSInteger power;  //功率值W
@property (nonatomic, assign) NSInteger min0;   //模式 0的最小值
@property (nonatomic, assign) NSInteger max0;   //模式 0的最大值
@property (nonatomic, assign) NSInteger min1;   //模式 1的最小值
@property (nonatomic, assign) NSInteger max1;   //模式 1的最大值
@property (nonatomic, assign) NSInteger high;   //模式 2的高值
@property (nonatomic, assign) NSInteger middle; //模式 2的中值
@property (nonatomic, assign) NSInteger low;    //模式 2的低值

@end

NS_ASSUME_NONNULL_END
