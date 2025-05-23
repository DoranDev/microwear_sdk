//
//  NJY_DeviceFunModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/8/24.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_DeviceFunModel : NSObject
@property (nonatomic, assign) NSInteger customVideoDial; //是否支持视频表盘 1：支持 0：不支持
@property (nonatomic, assign) NSInteger customDialVersion; //1支持无时间
@property (nonatomic, assign) NSInteger customSupportMedic; //1支持吃药提醒
@property (nonatomic, assign) NSInteger customSupportGpsSport; //是否支持GPS运动         1:支持 0:不支持
@property (nonatomic, assign) NSInteger customSupportUserHeadLogo; //是否支持用户头像，名称  0:不支持  非0:头像宽高(1Byte)
@property (nonatomic, assign) NSInteger customSupportAlbumw; //相册功能 0:不支持       非0:相册宽(1Byte)
@property (nonatomic, assign) NSInteger customSupportAlbumh; //相册功能 0:不支持       非0:相册高(1Byte)
@property (nonatomic, assign) NSInteger customSupportLyric; //支持歌词  0不支持 1支持



@end

NS_ASSUME_NONNULL_END
