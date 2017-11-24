'''
Created on Oct 28, 2016

@author: Benjamin Hodgson
'''

import math

def factorNumber(number):
    '''
    returns the unique prime factors for a number
    '''
    count_num = 0
    square_num = math.ceil(math.sqrt(number))
    while True:
        if count_num == number:
            break
        square_check = square_num**2 - number
        if math.sqrt(square_check) == math.ceil(math.sqrt(square_check)):
            return int((square_num + math.sqrt(square_check))), int((square_num - math.sqrt(square_check))) 
        square_num += 1
        count_num += 1
    return "There are no factors!"

if __name__ == '__main__':
    print "The factors of 223,822,733 are", factorNumber(223822733)
    print "The factors of 327653 are", factorNumber(327653)
    print "The factors of 33 are", factorNumber(33)