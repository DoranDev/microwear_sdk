//
//  NJY_GameModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/10/19.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_GameModel : NSObject

@property (nonatomic, assign) NSInteger aid; //游戏id
@property (nonatomic, assign) NSInteger state; // 1:支持并未安装  3:支持并已安装

@end

NS_ASSUME_NONNULL_END
