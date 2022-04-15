"""
Aplication of printing data from simulation of mining to xml

Program is executed by command: python3 ./run_sp2.py -i <vstupni soubor> -o <vystupni soubor>

format of <vstupni soubor> is .txt generated from java program of simulation of mining in mine
"""

import sys
import xml.etree.ElementTree as ET
import xml.dom.minidom
import datetime

from Farmer import Farmer
from Ferry import Ferry
from Lorry import Lorry
from Worker import Worker

TIME_INDEX = 0
ROLE_INDEX = 1
THREAD_INDEX = 2
DESCRIPTION_INDEX = 3

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
        roles.add(x[ROLE_INDEX])
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

def assignRowsToStaff(parsedData, workers, lorrys, farmers, ferrys):
    for line in parsedData:
        role = line[ROLE_INDEX].split(" ")
        if role[0].__contains__("Worker"):
            workers.__getitem__(int(role[1]) - 1).processData(line[DESCRIPTION_INDEX])
        elif role[0].__contains__("Lorry"):
            lorrys.__getitem__(int(role[1]) - 1).printName()
        elif role[0].__contains__("Farmer"):
            farmers.__getitem__(int(role[1]) - 1).processData(line[DESCRIPTION_INDEX])
        elif role[0].__contains__("Ferry"):
            ferrys.__getitem__(int(role[1]) - 1).processData(line[DESCRIPTION_INDEX])  

def readTime(start, end):
    """
    Method compute difference between two times
    :param start: start of the time interval
    :param end: end of the time interval
    :return: difference between start and end
    """
    return datetime.datetime.strptime(end, "%d/%m/%Y %H:%M:%S.%f") - datetime.datetime.strptime(start, "%d/%m/%Y %H:%M:%S.%f")

def averageFerryWaitTime(ferrys):
    """
    Method counts average wait time of ferry
    :param ferrys: all records of ferry transport
    :return: average waiting time of ferry
    """
    totalTime = 0
    for ferry in ferrys:
        totalTime += ferry.waitTime
    return float(totalTime)/len(ferrys)

def writeXML(parsedData, workers, lorrys, farmers, ferrys):
    """
    Method writes data to the XML file
    :param parsedData:
    :return:
    """
    timeOfSimulation = readTime(parsedData[0][0], parsedData[len(parsedData) - 1][0])
    timeOfSimulationString = str(timeOfSimulation)[:-3]

    # root element
    root = ET.Element('Simulation', {'duration': timeOfSimulationString})

    blocks = ET.SubElement(root, 'blockAverageDuration', {'totalCount':str(farmers[0].numberOfBlocks)})

    blocks.text = str(10)

    source = ET.SubElement(root, 'resourceAverageDuration', {'totalCount':str(farmers[0].numberOfSources)})

    source.text = str(20)

    timeFerry = averageFerryWaitTime(ferrys)
    print(timeFerry)
    avgFerry = ET.SubElement(root, 'ferryAverageWait', {'trips':str(len(ferrys))}).text = "{:.2f}".format(timeFerry)

    # write to file
    #tree = ET.ElementTree(root)
    #tree.write(sys.argv[4], xml_declaration=True, encoding='utf-8')
    dom = xml.dom.minidom.parseString(ET.tostring(root))
    xml_string = dom.toprettyxml()
    f = open(sys.argv[4], "w")
    f.write(xml_string)
    f.close()

if __name__ == '__main__':
    print('Argument List:', str(sys.argv))

    data = readFile(sys.argv[2])
    parsedData = parseData(data)

    roles = idetifyRoles(parsedData)

    workers, lorrys, farmers, ferrys = createInstances(roles)

    assignRowsToStaff(parsedData, workers, lorrys, farmers, ferrys)

    writeXML(parsedData, workers, lorrys, farmers, ferrys)