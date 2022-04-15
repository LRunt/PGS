"""
Aplication of printing data from simulation of mining to xml

Program is executed by command: python3 ./run_sp2.py -i <vstupni soubor> -o <vystupni soubor>

format of <vstupni soubor> is .txt generated from java program of simulation of mining in mine
"""

import sys
import xml.etree.ElementTree as ET
import datetime

from Farmer import Farmer
from Ferry import Ferry
from Lorry import Lorry
from Worker import Worker


def indent(elem, level=0):
    """
    Method manage visual of the xml file
    copied from building xml tutorial
    """
    i = "\n" + level*"  "
    j = "\n" + (level-1)*"  "
    if len(elem):
        if not elem.text or not elem.text.strip():
            elem.text = i + "  "
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
        for subelem in elem:
            indent(subelem, level+1)
        if not elem.tail or not elem.tail.strip():
            elem.tail = j
    else:
        if level and (not elem.tail or not elem.tail.strip()):
            elem.tail = j
    return elem

def readFile(fileName):
    """
    Method reads a file and returns array of lines
    :param fileName: name of file which is reading
    :return: data from the input file
    """
    f = open(fileName, 'r')
    data = f.read()
    f.close()
    return data

def parseData(data):
    """
    Method parse data and returns two-dimensional array
    where first dimension are lines
    second dimensions are atributes in the following order: time, role, thread, describtion
    :param data: input data - array of lines from input file
    :return: two-dimensional array of parsed data
    """
    records = data.split('\n');
    parsedData = []
    for i in range(len(records)):
        atribut = records[i].split("><")
        for j in range(len(atribut)):
            atribut[j] = atribut[j].replace("<","")
            atribut[j] = atribut[j].replace(">","")
        parsedData.append(atribut)
    if parsedData[len(parsedData) - 1][0] == (""):
        del parsedData[-1]
    return parsedData

def idetifyRoles(parsedData):
    """
    Method identifies roles and writes them to the role set
    :param parsedData: two-dimensional array of parsed data
    :return: set of roles, which the parsed data contains
    """
    roles = set()
    i = 0
    for x in parsedData:
        roles.add(x[1])
    return roles

def createInstances(roles):
    """
    Method creates instances of all roles (staff) from the set
    :param roles: set of roles (staff)
    :return: arrays of all workers, lorrys, farmers and ferrys
    """
    workers = []
    lorrys = []
    farmers = []
    ferrys = []
    for role in roles:
        if role.__contains__("Worker"):
            workers.append(Worker(role))
        elif role.__contains__("Lorry"):
            lorrys.append(Lorry(role))
        elif role.__contains__("Farmer"):
            farmers.append(Farmer(role))
        elif role.__contains__("Ferry"):
            ferrys.append(Ferry(role))

    workers.sort(key=lambda x: x.name)
    lorrys.sort(key=lambda x: x.name)
    farmers.sort(key=lambda x: x.name)
    ferrys.sort(key=lambda x: x.name)
    return workers, lorrys, farmers, ferrys

#def assignRowsToStaff():


def readTime(start, end):
    """
    Method compute difference between two times
    :param start: start of the time interval
    :param end: end of the time interval
    :return: difference between start and end
    """
    return datetime.datetime.strptime(end, "%d/%m/%Y %H:%M:%S.%f") - datetime.datetime.strptime(start, "%d/%m/%Y %H:%M:%S.%f")

def writeXML(parsedData):
    """
    Method writes data to the XML file
    :param parsedData:
    :return:
    """
    timeOfSimulation = readTime(parsedData[0][0], parsedData[len(parsedData) - 1][0])
    timeOfSimulationString = str(timeOfSimulation)[:-3]

    root = ET.Element('Simulation', {'duration': timeOfSimulationString})

    # root element
    root = ET.Element('Simulation', {'duration': timeOfSimulationString})

    # write to file
    tree = ET.ElementTree(indent(root))
    tree.write(sys.argv[4], xml_declaration=True, encoding='utf-8')

if __name__ == '__main__':
    print('Argument List:', str(sys.argv))

    data = readFile(sys.argv[2])
    parsedData = parseData(data)

    roles = idetifyRoles(parsedData)

    workers, lorrys, farmers, ferrys = createInstances(roles)

    writeXML(parsedData)