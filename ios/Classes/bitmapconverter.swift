

import UIKit

extension UIImage {
    
    /// Get the pixel data of the image
    func getPixelRGBAData() -> Data {
        guard let imgRef = self.cgImage else {
            return Data()
        }
        
        let width = imgRef.width
        let height = imgRef.height
        let rect = CGRect(x: 0, y: 0, width: width, height: height)
        let bytesPerPixel = 4   // Four components per pixel: A, R, G, B
        let bytesPerRow = bytesPerPixel * width // Row width * components per pixel
        let bitsPerComponent = 8 // 8 bits per component
        
        // Allocate memory for raw data
        let rawBytes = calloc(width * height * bytesPerPixel, MemoryLayout<UInt8>.size)
        
        // Create color space
        let colorSpace = CGColorSpaceCreateDeviceRGB()
        
        guard let context = CGContext(
            data: rawBytes,
            width: width,
            height: height,
            bitsPerComponent: bitsPerComponent,
            bytesPerRow: bytesPerRow,
            space: colorSpace,
            bitmapInfo: CGImageAlphaInfo.premultipliedLast.rawValue | CGBitmapInfo.byteOrder32Big.rawValue
        ) else {
            free(rawBytes)
            return Data()
        }
        
        // Draw image into context
        context.draw(imgRef, in: rect)
        
        let mData = NSMutableData()
        
        // Convert the pixel data
        for y in 0..<height {
            for x in 0..<width {
                let index = bytesPerRow * y + bytesPerPixel * x
                guard let rawBytesTyped = rawBytes?.bindMemory(to: UInt8.self, capacity: width * height * bytesPerPixel) else {
                    continue
                }
                
                let r = UInt(rawBytesTyped[index])
                let g = UInt(rawBytesTyped[index + 1])
                let b = UInt(rawBytesTyped[index + 2])
                //let a = UInt(rawBytesTyped[index + 3])
                
                let all = ((r >> 3) << 11) + ((g >> 2) << 5) + (b >> 3)
                var bytes: [UInt8] = [0, 0]
                
                bytes[1] = UInt8(all % 256)
                bytes[0] = UInt8(all / 256)
                mData.append(Data(bytes))
            }
        }
        
        // Clean up
        free(rawBytes)
        
        return mData as Data
    }
    
    /// Convert image file at path to bitmap data
    /// - Parameter path: Path to the image file
    /// - Returns: Bitmap data or nil if conversion failed
    static func getBitmapDataFromImageFile(atPath path: String) -> Data? {
        // Check if file exists
        guard FileManager.default.fileExists(atPath: path) else {
            print("Error: File doesn't exist at path \(path)")
            return nil
        }
        
        // Create UIImage from file path
        guard let image = UIImage(contentsOfFile: path) else {
            print("Error: Failed to create UIImage from file at path \(path)")
            return nil
        }
        
        // Get bitmap data
        let bitmapData = image.getPixelRGBAData()
        
        return bitmapData
    }
}

// Usage example:
// if let bitmapData = UIImage.getBitmapDataFromImageFile(atPath: "/path/to/your/image.jpg") {
//     // Use the bitmap data
//     print("Successfully converted image to bitmap data, size: \(bitmapData.count) bytes")
// } else {
//     print("Failed to convert image to bitmap data")
// }
