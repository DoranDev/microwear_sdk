//
//  UIImage+Bitmap.m
//  NJYWatchDemo
//
//  Created by Michael on 2022/7/22.
//

#import "UIImage+Bitmap.h"

@implementation UIImage (Bitmap)

/// 获取图片的像素数据
- (NSData *)getPixelRGBAData {
    CGImageRef imgRef = self.CGImage;
    NSUInteger width = CGImageGetWidth(imgRef);
    NSUInteger height = CGImageGetHeight(imgRef);
    CGRect rect = CGRectMake(0, 0, width, height);
    NSUInteger bytesPerPixel = 4;   // 一个像素四个分量，即A、R、G、B
    NSUInteger bytesPerRow = bytesPerPixel * width; //行宽 * 单个像素的分量
    NSUInteger bitsPerComponent = 8; // 每个分量8位
    unsigned char *rawBytes = (unsigned char *)calloc(width * height * bytesPerPixel, sizeof(unsigned char));
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    
    /*
     参数1：数据源
     参数2：图片宽
     参数3：图片高
     参数4：表示每一个像素点，每一个分量大小
     在我们图像学中，像素点：A,R,G,B四个组成 每一个表示一个分量（例如，A，R，G，B）
     ！！！！--------->>>>>>>>>在我们计算机图像学中每一个分量的大小是8个字节
     参数5：每一行大小（其实图片是由像素数组组成的）
     如何计算每一行的大小，所占用的内存
     首先计算每一个像素点大小（我们取最大值）： ARGB是4个分量 = 每个分量8个字节 * 4
     参数6:颜色空间
     参数7:是否需要透明度
     */
    CGContextRef context = CGBitmapContextCreate(rawBytes, width, height, bitsPerComponent, bytesPerRow, colorSpace, kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);
    
    CGContextDrawImage(context, rect, imgRef);
    
    NSMutableData *mData = [NSMutableData data];
    
    for (int y=0; y<height; y++) {
        for (int x=0; x<width; x++) {
            NSUInteger index = bytesPerRow * y + bytesPerPixel * x;
            NSUInteger r = rawBytes[index];
            NSUInteger g = rawBytes[index + 1];
            NSUInteger b = rawBytes[index + 2];
            //NSUInteger a = rawBytes[index + 3];
            
            NSInteger all = ((r >> 3) << 11) + ((g >> 2) << 5) + (b >> 3);
            Byte bytes[2];
            
            bytes[1] = all % 256;
            bytes[0] = all / 256;
            [mData appendData:[NSData dataWithBytes:bytes length:2]];
        }
    }
    
    CGContextRelease(context);
    CGColorSpaceRelease(colorSpace);
    free(rawBytes);
    
    return mData;
}


@end
