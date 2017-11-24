'''
Created on Oct 28, 2016

@author: Benjamin Hodgson
'''

import math

def isPrime(number):
    '''
    Returns True if number is prime, false if number is composite 
    '''
    limit = int(math.sqrt(number))+1
    if number < 2:
        return False
    if number<4:
        return True
    if number %2 == 0:
        return False
    for n in range(3,limit, 2):  
        if number/n * n == number:
            return False
    return True