//
//  UIImage+Bitmap.h
//  NJYWatchDemo
//
//  Created by Michael on 2022/7/22.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIImage (Bitmap)
/// 获取图片的像素数据
- (NSData *)getPixelRGBAData;

@end

NS_ASSUME_NONNULL_END
