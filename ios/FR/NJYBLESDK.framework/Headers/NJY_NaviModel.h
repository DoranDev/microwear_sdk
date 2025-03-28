//
//  NJY_NaviModel.h
//  NJYBLESDK
//
//  Created by Michael on 2023/8/3.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_NaviModel : NSObject

@property (nonatomic, assign) NSInteger mapType;    //0:结束 1:高德 2:百度 3. 谷歌
@property (nonatomic, assign) NSInteger mapImgType; //图标类型 （参考 ：高德地图.h）
@property (nonatomic, assign) NSInteger reserve1;   //预留1  暂时填 ：0
@property (nonatomic, assign) NSInteger reserve2;   //预留2  暂时填 ：0
@property (nonatomic, assign) NSInteger dis_m;      //剩余距离（米）
@property (nonatomic, assign) NSInteger lenth;      //道路提示语长度 byte,最多100byte
@property (nonatomic, strong) NSString * name_road; //字符长度

@end

NS_ASSUME_NONNULL_END
