'''
Created on Oct 28, 2016

@author: Benjamin Hodgson
'''

def find_modinverse(number, mod):
    '''
    Calculates the modular inverse of a number, number, 
    mod a modulo, mod.
    '''
    number_count = 0
    answer = 0
    while True:
        answer = number*number_count % mod
        if answer == 1:
            return number_count
        number_count += 1
        
def printmod1(number, mod):
    '''
    prints the solutions for x^number = 1 (mod). 
    '''
    number_count = 0
    answer = 0
    set_length = 0
    set_count = set([])
    while True:
        answer += 1
        set_length = len(set_count)
        if number_count**number % mod == 1:
            set_count.add(number_count % mod)
            #print number_count
        if answer == mod:
            if set_length == len(set_count):
                return sorted(list(set_count))
            else:
                answer = 0
        number_count += 1
        
if __name__ == '__main__':
    print "Trying to find integer values for x when working modulo an integer n that satisfy: x^2 = 1 (n)"
    print 
    print "When n is 6, possible values for x are:", printmod1(2, 6), "There are", len(printmod1(2, 6)), "solutions."
    print "When n is 30, possible values for x are:", printmod1(2, 30), "There are", len(printmod1(2, 30)), "solutions."
    print "When n is 210, possible values for x are:", printmod1(2, 210), "There are", len(printmod1(2, 210)), "solutions."
    print "When n is 2310, possible values for x are:", printmod1(2, 2310), "There are", len(printmod1(2, 2310)), "solutions."
    print "When n is 8, possible values for x are:", printmod1(2, 8), "There are", len(printmod1(2, 8)), "solutions."