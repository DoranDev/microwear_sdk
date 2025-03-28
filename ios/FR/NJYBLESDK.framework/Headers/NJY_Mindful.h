//
//  NJY_Mindful.h
//  NJYBLESDK
//
//  Created by Michael on 2024/1/16.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_Mindful : NSObject
@property (nonatomic, assign) NSInteger timestamp;  //角度
@property (nonatomic, assign) NSInteger type;  //0:目前心情  1:全天心情
@property (nonatomic, assign) NSInteger mindful_type;  //0:非常不愉快 1:不愉快 2:有点不愉快 3:不悲不喜4:有点愉快 5:愉快 6:非常愉快

@end

NS_ASSUME_NONNULL_END
