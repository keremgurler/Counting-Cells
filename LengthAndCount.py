from __future__ import print_function
from __future__ import division
import cv2 as cv
import numpy as np
import argparse
from math import atan2, cos, sin, sqrt, pi

    
def getLength(pts, cntr , order):
	sz = len(pts) 
	max = sqrt(((int(pts[0,0,0])) - cntr[0])**2 + ((int(pts[0,0,1])) - cntr[1])**2)
	for i in range(sz):
		temp = sqrt(((int(pts[i,0,0])) - cntr[0])**2 + ((int(pts[i,0,1])) - cntr[1])**2)
		if (max < temp):
			max = temp	
        
	print(str(order) +" "+ str(max*2))
	return max
	
def getOrientation(pts, img, order):
    
    sz = len(pts)
    	
    data_pts = np.empty((sz, 2), dtype=np.float64)
    for i in range(data_pts.shape[0]):
        data_pts[i,0] = pts[i,0,0]
        data_pts[i,1] = pts[i,0,1]
    # Perform PCA analysis
	
    mean = np.empty((0))
    mean, eigenvectors, eigenvalues = cv.PCACompute2(data_pts, mean)
    # Store the center of the object
    cntr = (int(mean[0,0]), int(mean[0,1]))
    getLength(pts,cntr, order)    
    font = cv.FONT_HERSHEY_SIMPLEX
    cv.putText(src,str(order),cntr, font, 0.5,(0,255,255),1,cv.LINE_AA)
    #cv.circle(img, cntr, 3, (255, 0, 255), 2)
    p1 = (cntr[0] + 0.02 * eigenvectors[0,0] * eigenvalues[0,0], cntr[1] + 0.02 * eigenvectors[0,1] * eigenvalues[0,0])
    p2 = (cntr[0] - 0.02 * eigenvectors[1,0] * eigenvalues[1,0], cntr[1] - 0.02 * eigenvectors[1,1] * eigenvalues[1,0])
    angle = atan2(eigenvectors[0,1], eigenvectors[0,0]) # orientation in radians
		
    return angle
parser = argparse.ArgumentParser(description='Code for Introduction to Principal Component Analysis (PCA) tutorial.\
                                              This program demonstrates how to use OpenCV PCA to extract the orientation of an object.')
parser.add_argument('--input', help='Path to input image.', default='pca_test1.jpg')
parser.add_argument('--output', help='Path to output image',)
args = parser.parse_args()
src = cv.imread(cv.samples.findFile(args.input))

# Check if image is loaded successfully
if src is None:
    print('Could not open or find the image: ', args.input)
    exit(0)

# Convert image to grayscale
gray = cv.cvtColor(src, cv.COLOR_BGR2GRAY)
# Convert image to binary
_, bw = cv.threshold(gray, 50, 255, cv.THRESH_BINARY | cv.THRESH_OTSU)
contours, _ = cv.findContours(bw, cv.RETR_LIST, cv.CHAIN_APPROX_NONE)

count = 0
for i, c in enumerate(contours):
    # Calculate the area of each contour
    area = cv.contourArea(c);
    # Ignore contours that are too small or too large
    if area < 1e2 or 1e5 < area:
        continue
    # Draw each contour only for visualization purposes	
    cv.drawContours(src, contours, i, (0, 0, 255), 1); 
    count += 1
    	
    # Find the orientation of each shape
    getOrientation(c, src , count)
	
cv.imshow('output', src)
print(count)
cv.waitKey()