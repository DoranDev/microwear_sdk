//
//  NJY_DailInfoModel.h
//  NJYBLESDK
//
//  Created by Michael on 2022/7/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NJY_DailInfoModel : NSObject

@property (nonatomic, assign) NSInteger dateLocation;
@property (nonatomic, assign) NSInteger dateTopPosition;
@property (nonatomic, assign) NSInteger dateBelowPosition;
@property (nonatomic, assign) NSInteger colorR;
@property (nonatomic, assign) NSInteger colorG;
@property (nonatomic, assign) NSInteger colorB;
@property (nonatomic, assign) NSInteger screenHigh;
@property (nonatomic, assign) NSInteger screenWidth;
@property (nonatomic, assign) NSInteger imageHigh;
@property (nonatomic, assign) NSInteger imageWidth;
@property (nonatomic, strong) NSData *imageBg;
@property (nonatomic, strong) NSData *thumbnailImage;
@property (nonatomic, assign) NSInteger dailType;
@property (nonatomic, assign) NSInteger type;//0=RGB 1=jpg


@end

NS_ASSUME_NONNULL_END
