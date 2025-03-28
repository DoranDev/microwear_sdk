//
//  NJY_MotionGameModel.h
//  NJYBLESDK
//
//  Created by Michael on 2023/1/31.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_MotionGameModel : NSObject
@property (nonatomic, assign) short x;
@property (nonatomic, assign) short y;
@property (nonatomic, assign) short Speed;
@property (nonatomic, assign) short X_Throw;
@property (nonatomic, assign) short Y_Throw;
@property (nonatomic, assign) short Speed_Throw;
@property (nonatomic, assign) short Count_Throw;
@property (nonatomic, assign) short X_Move_Distance;
@property (nonatomic, assign) short Y_Move_Distance;
@property (nonatomic, assign) short X_gravity;
@property (nonatomic, assign) short Y_gravity;
@property (nonatomic, assign) short Z_gravity;

@end

NS_ASSUME_NONNULL_END
