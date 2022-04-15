import sys
import xml.etree.ElementTree as ET
import datetime

from Farmer import Farmer
from Ferry import Ferry
from Lorry import Lorry
from Worker import Worker


def indent(elem, level=0):
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
    f = open(fileName, 'r')
    data = f.read()
    f.close()
    return data

def parseData(data):
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
    roles = set()
    i = 0
    for x in parsedData:
        roles.add(x[1])
    return roles

def createInstances(roles):
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
    return workers, lorrys, farmers, ferrys

def readTime(start, end):
    return datetime.datetime.strptime(end, "%d/%m/%Y %H:%M:%S.%f") - datetime.datetime.strptime(start, "%d/%m/%Y %H:%M:%S.%f")

def writeXML(parsedData):
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

    createInstances(roles)

    writeXML(parsedData)