'''
Created on Dec 4, 2016

@author: Benjamin Hodgson
'''

import math
import random

def find_k(n):
    '''
    find k that satisfies the equation:
    n-1 = (2^k)*q, q odd
    '''
    val = n-1
#     print val
    k_check = 0
    q_check = 1
#     loop_count = 0
    while True:
#         print "k", k_check
#         print "q", q_check
#         if loop_count == 50:
#             break
        while True:
            if (2**k_check)*q_check == val:
                return (k_check, q_check)
            elif (2**k_check)*q_check > val:
                q_check = 1
                break
            else:
                q_check += 2
        k_check += 1
#         loop_count += 1
        
def Miller_Rabin(t, n):
    '''
    Goes through t number of trials to probabilistically 
    determine the likelihood that n is prime.
    '''
    a = random.randint(2,n-1)
    loop_count = 0
    while True:
#         print loop_count
        num_val = find_k(n)
        k = num_val[0]
#         print "k", k
        q = num_val[1]
#         print "q", q
        if loop_count == t:
            return str(n) + " is prime with probability at least " + str(1-(1/4.0)**loop_count)
        if a == n:
            return str(n) + " is prime"
#         print "a", a
        a_check = a**q
#         print "a_check", a_check
        if a_check % n == 1:
            a = random.randint(2,n-1)
        else:
            i_count = 0
            for i in range(k):
                if (a**((2**i)*q)) % n == n-1:
                    i_count += 1
#                 print (a**((2**i)*q)) % n
#                 print (a**((2**i)*q))
            if i_count == 0:
                return str(n) + " is composite"
            else:
                a = random.randint(2,n-1)
        loop_count += 1

print Miller_Rabin(10, 101089)
print Miller_Rabin(10, 280001)
print Miller_Rabin(10, 104717)
print Miller_Rabin(10, 3057601)
print Miller_Rabin(10, 577757)
print Miller_Rabin(10, 252601)
print Miller_Rabin(2, 425749)