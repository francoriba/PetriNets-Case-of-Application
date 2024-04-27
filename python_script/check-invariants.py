import re
import io

txt = ''

for i in open("log.txt", "r"):
    txt = txt + i

def invariants(txt):
    line = [txt, 0]
    # print(line[0] + '\n')
    while(True) :

        # extracting specific parts of the sequence found by the regular expression and replacing it
        line = re.subn('((T1)(.*?)((T2)(.*?)(T4)(.*?)(T6)(.*?)|(T3)(.*?)(T5)(.*?)(T7)(.*?))(T8)(.*?))|(T9)(.*?)(TA)(.*?)(TB)(.*?)(TC)(.*?)',
	    '\g<3>\g<6>\g<8>\g<10>\g<12>\g<14>\g<16>\g<18>\g<20>\g<22>\g<24>\g<26>', line[0])
        
        print(line)
        print('\n')

        if(line[1] == 0):
            break

    if(line[0] == ""):
        print("Success! All invariants are fulfilled\n")
    else:
        print("Incomplete sequences found:\n")
        print(line[0])

invariants(txt)
