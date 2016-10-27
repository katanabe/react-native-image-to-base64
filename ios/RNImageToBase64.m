#import "RNImageToBase64.h"
#import <AssetsLibrary/AssetsLibrary.h>
#import <UIKit/UIKit.h>

@implementation RNImageToBase64

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(getBase64String:(NSDictionary *)options callback:(RCTResponseSenderBlock)callback)
{
  NSString *url = [options valueForKey:@"url"];

  ALAssetsLibrary *library = [[ALAssetsLibrary alloc] init];
  [library assetForURL:[[NSURL alloc] initWithString:url] resultBlock:^(ALAsset *asset) {
    ALAssetRepresentation *rep = [asset defaultRepresentation];
    CGImageRef imageRef = [rep fullScreenImage];
    UIImage *img = [UIImage imageWithCGImage:imageRef];
    NSData *imageData = UIImageJPEGRepresentation(img, [[options valueForKey:@"quality"] floatValue]);
    NSString *base64Encoded = [imageData base64EncodedStringWithOptions:0];
    callback(@[[NSNull null], base64Encoded]);

  } failureBlock:^(NSError *error) {
    NSLog(@"that didn't work %@", error);
    callback(@[error]);

  }];
}

@end
