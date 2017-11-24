'''
Created on Nov 18, 2016

@author: Benjamin Hodgson
'''
import math

'''
***Binary Operator Examples***
i = 3
binary = '{0:b}'.format(i)
print binary
print type("{0:b}".format(i))
number = int(binary, 2)
print number
'''

def binaryXOR(string1, string2, mod):
    '''
    takes two binary strings and performs
    binary addition. Returns the resulting 
    binary string mod the parameter mod
    ***EXAMPLE***
    a = "11011111101100110110011001011101000"
    b = "11001011101100111000011100001100001"
    y = int(a,2) ^ int(b,2)
    print '{0:b}'.format(y)
    '''
    eval_xor = (int(string1,2)^int(string2,2))%mod
    finalresult = '{0:b}'.format(eval_xor)
    while True:
        #make sure the result length matches the string length
        if len(finalresult) != int(math.log(mod)/math.log(2)):
            finalresult = "0"+finalresult
        else:
            break
    return finalresult

def find_multinverse(number, mod):
    '''
    Calculates the multiplicative inverse of a number, number, 
    mod a modulus, mod.
    '''
    number_count = 0
    answer = 0
    while True:
        answer = number*number_count % mod
        if answer == 1:
            return number_count
        number_count += 1
    
def find_addinverse(number, mod):
    '''
    calculates the additive inverse of a number, number,
    mod a modulus, mod.
    '''
    return -number%mod

def modArithmetic(string1, string2, mod):
    '''
    takes two binary strings and converts them 
    to integers. Performs modular arithmetic
    mod the parameter mod and returns the 
    equivalent binary string
    '''
    num1 = int(string1,2)
    num2 = int(string2,2)
    result_num = (num1 + num2)%mod
    finalresult = '{0:b}'.format(result_num)
    while True:
        #make sure the result length matches the string length
        if len(finalresult) != int(math.log(mod)/math.log(2)):
            finalresult = "0"+finalresult
        else:
            break
    return finalresult

def invmodArithmetic(string1, key, mod):
    '''
    takes two binary strings, but uses the inverse
    of the key when performing modular arithmetic
    '''
    num1 = int(string1,2)
    num2 = find_addinverse(int(key,2), mod)
    result_num = (num1 + num2)%mod
    finalresult = '{0:b}'.format(result_num)
    while True:
        #make sure the result length matches the string length
        if len(finalresult) != int(math.log(mod)/math.log(2)):
            finalresult = "0"+finalresult
        else:
            break
    return finalresult
    
def modMultiplication(string1, string2, mod):
    '''
    Takes two binary strings and converts them
    to integers. Performs modular multiplication
    mod the parameter mod + 1 and returns the 
    equivalent binary string
    '''   
    mult_mod = mod+1
    num1 = int(string1,2)
    num2 = int(string2,2)
    if num1 == 0:
        num1 = mod
    if num2 == 0:
        num2 = mod
    result_num = (num1*num2)%mult_mod
    if result_num == mod:
        result_num = 0
    finalresult = '{0:b}'.format(result_num)
    # print mult_mod
    while True:
        #make sure the result length matches the string length
        if len(finalresult) != int(math.log(mod)/math.log(2)):
            finalresult = "0"+finalresult
        else:
            break
    return finalresult

def invmodMultiplication(string1, key, mod):
    '''
    Takes two binary strings and converts them
    to integers. Finds the inverse of the key and
    Performs modular multiplication
    mod the parameter mod + 1 and returns the 
    equivalent binary string
    '''   
    mult_mod = mod+1
    num1 = int(string1,2)
    print "encryption key used:", key, int(key,2)
    num2 = find_multinverse(int(key,2), mult_mod)
    print "inverse of encryption key used:", num2, '{0:b}'.format(num2)
    if num1 == 0:
        num1 = mod
    if num2 == 0:
        num2 = mod
    result_num = (num1*num2)%mult_mod
    # print result_num
    if result_num == mod:
        result_num = 0
    finalresult = '{0:b}'.format(result_num)
    # print mult_mod
    while True:
        #make sure the result length matches the string length
        if len(finalresult) != int(math.log(mod)/math.log(2)):
            finalresult = "0"+finalresult
        else:
            break
    # print finalresult
    return finalresult

def roundIDEA(string, key, mod):
    '''
    performs one round of IDEA:
    1. Multiply X1 and the first subkey Z1.
    2. Add X2 and the second subkey Z2.
    3. Add X3 and the third subkey Z3.
    4. Multiply X4 and the fourth subkey Z4.
    5. Bitwise XOR the results of steps 1 and 3.
    6. Bitwise XOR the results of steps 2 and 4.
    7. Multiply the result of step 5 and the fifth subkey Z5.
    8. Add the results of steps 6 and 7.
    9. Multiply the result of step 8 and the sixth subkey Z6.
    10. Add the results of steps 7 and 9.
    11. Bitwise XOR the results of steps 1 and 9.
    12. Bitwise XOR the results of steps 3 and 9.
    13. Bitwise XOR the results of steps 2 and 10.
    14. Bitwise XOR the results of steps 4 and 10.
    '''
    string_length = int(math.log(mod)/math.log(2))
    print "string length:", string_length
    x1 = string[:string_length]
    x2 = string[string_length:string_length*2]
    x3 = string[string_length*2:string_length*3]
    x4 = string[string_length*3:]
    z1 = key[:string_length]
    z2 = key[string_length:string_length*2]
    z3 = key[string_length*2:string_length*3]
    z4 = key[string_length*3:string_length*4]
    z5 = key[string_length*4:string_length*5]
    z6 = key[string_length*5:string_length*6]
#     print string
#    print x1
#     print x2
#     print x3
#     print x4
#     print
#     print key
#     print z1
#     print z2
#     print z3
#     print z4
#     print z5
#     print z6 
    print "Starting step 1: Multiply X1 and the first subkey Z1"
    result1 = modMultiplication(x1, z1, mod)
    print "Result of step 1", result1  
    print "Starting step 2: Add X2 and the second subkey Z2"
    result2 = modArithmetic(x2, z2, mod)
    print "Result of step 2", result2
    print "Starting step 3: Add X3 and the third subkey Z3"
    result3 = modArithmetic(x3, z3, mod)
    print "Result of step 3", result3
    print "Starting step 4: Multiply X4 and the fourth subkey Z4"
    result4 = modMultiplication(x4, z4, mod)
    print "Result of step 4", result4  
    print "Starting step 5: Bitwise XOR the results of steps 1 and 3"
    result5 = binaryXOR(result1, result3, mod)
    print "Result of step 5", result5
    print "Starting step 6: Bitwise XOR the results of steps 2 and 4"
    result6 = binaryXOR(result2, result4, mod)
    print "Result of step 6", result6
    print "Starting step 7: Multiply the result of step 5 and the fifth subkey Z5"
    result7 = modMultiplication(result5, z5, mod)
    print "Result of step 7", result7  
    print "Starting step 8: Add the results of steps 6 and 7"
    result8 = modArithmetic(result6, result7, mod)
    print "Result of step 8", result8
    print "Starting step 9: Multiply the result of step 8 and the sixth subkey Z6"
    result9 = modMultiplication(result8, z6, mod)
    print "Result of step 9", result9
    print "Starting step 10: Add the results of steps 7 and 9"
    result10 = modArithmetic(result7, result9, mod)
    print "Result of step 10", result10
    print "Starting step 11: Bitwise XOR the results of steps 1 and 9"
    result11 = binaryXOR(result1, result9, mod)
    print "Result of step 11", result11  
    print "Starting step 12: Bitwise XOR the results of steps 3 and 9"
    result12 = binaryXOR(result3, result9, mod)
    print "Result of step 12", result12 
    print "Starting step 13: Bitwise XOR the results of steps 2 and 10"
    result13 = binaryXOR(result2, result10, mod)
    print "Result of step 13", result13 
    print "Starting step 14: Bitwise XOR the results of steps 4 and 10"
    result14 = binaryXOR(result4, result10, mod)
    print "Result of step 14", result14 
    #Finished round
    totalresult = result11+result13+result12+result14
    print "Finished round. The result is:", totalresult
    print
    return totalresult

def keygeneration(rounds, key, mod):
    '''
    returns a list of encryption keys used to 
    encrypt a messsage
    '''
    newkey = list(key)
    keylist = []
    allkeys = []
    allkeylen = int(math.log(mod)/math.log(2))
    print "key bit length:", allkeylen
    keysrequired = (rounds*6)+4
    print "keys required:", keysrequired
    keyshift = len(key)/5
    print "key shift:", keyshift
    loopcount = 0
    while True:
        if loopcount == keysrequired:
            break
        if loopcount%8 == 0 and loopcount>0:
            tempkey = []
            for index in range(len(newkey)):
                indexval = index + keyshift
                if indexval == len(newkey):
                    break
                tempkey.append(newkey[indexval])
            for index in range(keyshift):
                tempkey.append(newkey[index])
            newkey = tempkey
            #print tempkey
        allkeys.append("".join(newkey)[loopcount%8*allkeylen:loopcount%8*allkeylen+allkeylen])
        loopcount += 1
    keycount = 0
    tempkey = []
    print "all encryption keys:", allkeys
    while True:
        if keycount == keysrequired:
            break
        if keycount%6 == 0 and keycount>0:
            keylist.append("".join(tempkey))
            tempkey = []
        tempkey.append(allkeys[keycount])
        keycount += 1
    keylist.append("".join(tempkey))
    #print keylist
    return (keylist, allkeys)
    
def roundTransformation(string, key, mod):
    '''
    Performs the final transformation round:
    1. Multiply X1 and the first subkey.
    2. Add X2 and the second subkey.
    3. Add X3 and the third subkey.
    4. Multiply X4 and the fourth subkey.
    '''
    string_length = int(math.log(mod)/math.log(2))
    print "string length:", string_length
    x1 = string[:string_length]
    x2 = string[string_length:string_length*2]
    x3 = string[string_length*2:string_length*3]
    x4 = string[string_length*3:]
    z1 = key[:string_length]
    z2 = key[string_length:string_length*2]
    z3 = key[string_length*2:string_length*3]
    z4 = key[string_length*3:]
    print "Starting step 1: Multiply X1 and the first subkey Z1"
    result1 = modMultiplication(x1, z1, mod)
    print "Result of step 1", result1  
    print "Starting step 2: Add X2 and the second subkey Z2"
    result2 = modArithmetic(x2, z2, mod)
    print "Result of step 2", result2
    print "Starting step 3: Add X3 and the third subkey Z3"
    result3 = modArithmetic(x3, z3, mod)
    print "Result of step 3", result3
    print "Starting step 4: Multiply X4 and the fourth subkey Z4"
    result4 = modMultiplication(x4, z4, mod)
    print "Result of step 4", result4
    totalresult = result1+result2+result3+result4
    print "Finished round. The result is:", totalresult
    print
    return totalresult

def encryptIDEA(rounds, string, key, mod):
    '''
    encrypts a message using the number of 
    rounds from the integer parameter rounds, 
    plus the last transformation round.
    '''
    newstring = string
    print "Generating keys..."
    print
    genkeys = keygeneration(rounds, key, mod)
    keylist = genkeys[0]
    allkeylist = genkeys[1]
    print
    print "The generated key schedule:"
    print keylist
    print
    for number in range(rounds):
        roundnumber = number + 1
        print "*** STARTING ENCRYPTION ROUND", roundnumber, "***"
        print
        newstring = roundIDEA(newstring, keylist[number], mod)
    print "*** STARTING ENCRYPTION TRANSFORMATION ROUND ***"
    print
    newstring = roundTransformation(newstring, keylist[-1], mod)
    return (newstring, allkeylist)

def deroundIDEA(string, key, mod):
    '''
    performs one round of IDEA:
    1. Multiply X1 and the inverse of the first subkey Z1.
    2. Add X2 and the inverse of the second subkey Z2.
    3. Add X3 and the inverse of the third subkey Z3.
    4. Multiply X4 and the inverse of the fourth subkey Z4.
    5. Bitwise XOR the results of steps 1 and 3.
    6. Bitwise XOR the results of steps 2 and 4.
    7. Multiply the result of step 5 and the inverse of the fifth subkey Z5.
    8. Add the results of steps 6 and 7.
    9. Multiply the result of step 8 and the inverse of the sixth subkey Z6.
    10. Add the results of steps 7 and 9.
    11. Bitwise XOR the results of steps 1 and 9.
    12. Bitwise XOR the results of steps 3 and 9.
    13. Bitwise XOR the results of steps 2 and 10.
    14. Bitwise XOR the results of steps 4 and 10.
    '''
    string_length = int(math.log(mod)/math.log(2))
    print "string length:", string_length
    x1 = string[:string_length]
    x2 = string[string_length:string_length*2]
    x3 = string[string_length*2:string_length*3]
    x4 = string[string_length*3:]
    z1 = key[:string_length]
    z2 = key[string_length:string_length*2]
    z3 = key[string_length*2:string_length*3]
    z4 = key[string_length*3:string_length*4]
    z5 = key[string_length*4:string_length*5]
    z6 = key[string_length*5:string_length*6]
#     print string
#    print x1
#     print x2
#     print x3
#     print x4
#     print
#     print key
#     print z1
#     print z2
#     print z3
#     print z4
#     print z5
#     print z6 
    print "Starting step 1: Multiply X1 and the inverse of the first subkey Z1"
    result1 = invmodMultiplication(x1, z1, mod)
    print "Result of step 1", result1  
    print "Starting step 2: Add X2 and the inverse of the second subkey Z2"
    result2 = invmodArithmetic(x2, z2, mod)
    print "Result of step 2", result2
    print "Starting step 3: Add X3 and the inverse of the third subkey Z3"
    result3 = invmodArithmetic(x3, z3, mod)
    print "Result of step 3", result3
    print "Starting step 4: Multiply X4 and the inverse of the fourth subkey Z4"
    result4 = invmodMultiplication(x4, z4, mod)
    print "Result of step 4", result4  
    print "Starting step 5: Bitwise XOR the results of steps 1 and 3"
    result5 = binaryXOR(result1, result3, mod)
    print "Result of step 5", result5
    print "Starting step 6: Bitwise XOR the results of steps 2 and 4"
    result6 = binaryXOR(result2, result4, mod)
    print "Result of step 6", result6
    print "Starting step 7: Multiply the result of step 5 and the inverse of the fifth subkey Z5"
    result7 = invmodMultiplication(result5, z5, mod)
    print "Result of step 7", result7  
    print "Starting step 8: Add the results of steps 6 and 7"
    result8 = modArithmetic(result6, result7, mod)
    print "Result of step 8", result8
    print "Starting step 9: Multiply the result of step 8 and the inverse of the sixth subkey Z6"
    result9 = invmodMultiplication(result8, z6, mod)
    print "Result of step 9", result9
    print "Starting step 10: Add the results of steps 7 and 9"
    result10 = modArithmetic(result7, result9, mod)
    print "Result of step 10", result10
    print "Starting step 11: Bitwise XOR the results of steps 1 and 9"
    result11 = binaryXOR(result1, result9, mod)
    print "Result of step 11", result11  
    print "Starting step 12: Bitwise XOR the results of steps 3 and 9"
    result12 = binaryXOR(result3, result9, mod)
    print "Result of step 12", result12 
    print "Starting step 13: Bitwise XOR the results of steps 2 and 10"
    result13 = binaryXOR(result2, result10, mod)
    print "Result of step 13", result13 
    print "Starting step 14: Bitwise XOR the results of steps 4 and 10"
    result14 = binaryXOR(result4, result10, mod)
    print "Result of step 14", result14 
    #Finished round
    totalresult = result11+result13+result12+result14
    print "Finished round. The result is:", totalresult
    print
    return totalresult

def rounddeTransformation(string, key, mod):
    '''
    Performs the decryption final transformation round:
    1. Multiply X1 and the inverse of the first subkey.
    2. Add X2 and the inverse of the second subkey.
    3. Add X3 and the inverse of the third subkey.
    4. Multiply X4 and the inverse of the fourth subkey.
    '''
    string_length = int(math.log(mod)/math.log(2))
    print "string length:", string_length
    x1 = string[:string_length]
    x2 = string[string_length:string_length*2]
    x3 = string[string_length*2:string_length*3]
    x4 = string[string_length*3:]
    z1 = key[:string_length]
    z2 = key[string_length:string_length*2]
    z3 = key[string_length*2:string_length*3]
    z4 = key[string_length*3:]
    print "Starting step 1: Multiply X1 and the inverse of the first subkey Z1"
    result1 = invmodMultiplication(x1, z1, mod)
    print "Result of step 1", result1  
    print "Starting step 2: Add X2 and the inverse of the second subkey Z2"
    result2 = invmodArithmetic(x2, z2, mod)
    print "Result of step 2", result2
    print "Starting step 3: Add X3 and the inverse of the third subkey Z3"
    result3 = invmodArithmetic(x3, z3, mod)
    print "Result of step 3", result3
    print "Starting step 4: Multiply X4 and the inverse of the fourth subkey Z4"
    result4 = invmodMultiplication(x4, z4, mod)
    print "Result of step 4", result4
    totalresult = result1+result2+result3+result4
    print "Finished round. The result is:", totalresult
    print
    return totalresult

def decryptKeys(rounds, keylist, mod):
    '''
    creates a list of decryption keys, sort of.
    Divides the encryption key list into new
    sets for decryption keys. The inverse of these
    keys are found later
    '''
    dekeylist = []
    alldekeylist = []
    allkeylen = int(math.log(mod)/math.log(2))
    keysrequired = (rounds*6)+4
    # print "new key str:", newkeystr
    print "key bit length:", allkeylen
    print "keys required:", keysrequired
    for number in range(4):
        num_val = number-4 
        alldekeylist.append(keylist[num_val])
    alldekeylist.append(keylist[-6])
    alldekeylist.append(keylist[-5])
    for number in range(rounds-1):
        num_val = (rounds-1-number)*6
        print num_val
        alldekeylist.append(keylist[num_val])
        alldekeylist.append(keylist[num_val+2])
        alldekeylist.append(keylist[num_val+1])
        alldekeylist.append(keylist[num_val+3])
        alldekeylist.append(keylist[num_val-2])
        alldekeylist.append(keylist[num_val-1])
    alldekeylist.append(keylist[0])
    alldekeylist.append(keylist[1])
    alldekeylist.append(keylist[2])
    alldekeylist.append(keylist[3])
    print "all decryption keys:", alldekeylist
    keycount = 0
    tempkey = []
    while True:
        if keycount == keysrequired:
            break
        if keycount%6 == 0 and keycount>0:
            dekeylist.append("".join(tempkey))
            tempkey = []
        tempkey.append(alldekeylist[keycount])
        keycount += 1
    dekeylist.append("".join(tempkey))
    #print dekeylist
    return dekeylist
    

def decryptIDEA(rounds, string, keylist, mod):
    '''
    decrypts a message using the number of 
    rounds from the integer parameter rounds, 
    plus the last transformation round.
    '''
    newstring = string
    print "Generating decryption keys..."
    print
    dekeylist = decryptKeys(rounds, keylist, mod)
    print
    print "The generated decryption key schedule:"
    print dekeylist
    print
    for number in range(rounds):
        roundnumber = number + 1
        print "*** STARTING DECRYPTION ROUND", roundnumber, "***"
        print
        newstring = deroundIDEA(newstring, dekeylist[number], mod)
    print "*** STARTING DECRYPTION TRANSFORMATION ROUND ***"
    print
    newstring = rounddeTransformation(newstring, dekeylist[-1], mod)
    return (newstring,keylist)
      

if __name__ == '__main__':
#     print binaryXOR("0101", "0110", 2^4)
#     num = 2**16
#     print num
#     print math.log(num)/math.log(2)
#     print binaryXOR("1000", "1010", 16)
#     print modArithmetic("1100", "1100", 16)
#     print modMultiplication("1001", "1101", 16)
    encryptstring = "1001110010101100"
    keystring = "11011100011011110011111101011001"
    bitlength = 2**(len(encryptstring)/4)
    rounds = 4
    print "Encrypting", encryptstring, "using", keystring, "in", rounds, "rounds"
    print
    finishedencrypt = encryptIDEA(rounds, encryptstring, keystring, bitlength)
    print "The encrypted string is", finishedencrypt[0]
    # Now we need to generate the keys for decryption
    keylist = finishedencrypt[1]
    # print keylist
    print
    print "Decrypting", finishedencrypt[0], "in", rounds, "rounds"
    print
    finisheddecrypt = decryptIDEA(rounds, finishedencrypt[0], keylist, bitlength)
    print "The decrypted string is", finisheddecrypt[0]