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

def writeFile(fileName, fileData):
    """
    Method prints data ito the file
    :param fileName: name of file where the data is printed
    :param fileData: data what will be printed to the file
    """
    f = open(fileName, "w")
    f.write(fileData)
    f.close()

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
    """
    Method assigns the data to the corresponding instance and instance will take over the important data
    :param parsedData: two-dimensional array of parsed data
    :param workers: array of instances of workers
    :param lorrys: array of instances of lorrys
    :param farmers: array of instances of farmers
    :param ferrys: array of instances of ferrys
    """
    for line in parsedData:
        role = line[ROLE_INDEX].split(" ")
        if role[0].__contains__("Worker"):
            workers.__getitem__(int(role[1]) - 1).processData(line[DESCRIPTION_INDEX])
        elif role[0].__contains__("Lorry"):
            lorrys.__getitem__(int(role[1]) - 1).processData(line[DESCRIPTION_INDEX], line[TIME_INDEX])
        elif role[0].__contains__("Farmer"):
            farmers.__getitem__(int(role[1]) - 1).processData(line[DESCRIPTION_INDEX])
        elif role[0].__contains__("Ferry"):
            ferrys.__getitem__(int(role[1]) - 1).processData(line[DESCRIPTION_INDEX])  

def differenceOfTimeStamps(start, end):
    """
    Method compute difference between two times
    :param start: start of the time interval
    :param end: end of the time interval
    :return: difference between start and end in datetime format
    """
    return datetime.datetime.strptime(end, "%d/%m/%Y %H:%M:%S.%f") - datetime.datetime.strptime(start, "%d/%m/%Y %H:%M:%S.%f")

def differenceOfTimeStampsMS(start, end):
    """
    Method compute difference between two times
    :param start: star of the time interval
    :param end: end of the time interval
    :return: difference between start and end in milliseconds (integer)
    """
    startTimeStamp = datetime.datetime.strptime(start,'%d/%m/%Y %H:%M:%S.%f')
    endTimeStamp = datetime.datetime.strptime(end,'%d/%m/%Y %H:%M:%S.%f')
    startMillisec = startTimeStamp.timestamp() * 1000
    endMillisec = endTimeStamp.timestamp() * 1000
    return endMillisec - startMillisec

def averageTimeofMining(workers):
    """
    Method compute average time of mining one block and source
    :param workers: workers who was working in the mine
    :return: average time of mining one block, average time of mining one source, count of mined blocks, count of mined sources
    """
    minedBlocks = 0
    minedSources = 0
    totalTime = 0
    for worker in workers:
        minedBlocks += worker.numberOfMinedBlocks
        minedSources += worker.numberOfMinedSources
        totalTime += worker.timeOfMining

    averageMiningOneBlock = float(totalTime)/minedBlocks
    averageMiningOneSource = float(totalTime)/minedSources
    return averageMiningOneBlock, averageMiningOneSource, minedBlocks, minedSources

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

def generateXML(parsedData, workers, lorrys, farmers, ferrys):
    """
    Method generates data of the XML data
    :param parsedData:
    :return: string of generated XML
    """
    timeOfSimulation = differenceOfTimeStamps(parsedData[0][0], parsedData[len(parsedData) - 1][0])
    timeOfSimulationString = str(timeOfSimulation)[:-3]

    # root element - time of simulation
    root = ET.Element('Simulation', {'duration': timeOfSimulationString})
    # acquisition of data
    averageMiningOneBlock, averageMiningOneSource, minedBlocks, minedSources = averageTimeofMining(workers)
    # Average time of mining one block
    ET.SubElement(root, 'blockAverageDuration', {'totalCount':str(minedBlocks)}).text = "{:.2f}".format(averageMiningOneBlock)
    # Average time of mining one source
    ET.SubElement(root, 'resourceAverageDuration', {'totalCount':str(minedSources)}).text = "{:.2f}".format(averageMiningOneSource)
    # Average time of ferry waiting for 4 lorrys
    ET.SubElement(root, 'ferryAverageWait', {'trips':str(len(ferrys))}).text = "{:.2f}".format(averageFerryWaitTime(ferrys))
    #Workers
    workersElement = ET.SubElement(root, 'Workers')
    for i in range(len(workers)):
        worker = ET.SubElement(workersElement, 'Worker', {'id':str(i)})
        ET.SubElement(worker, 'resources').text = str(workers[i].numberOfMinedBlocks)
        ET.SubElement(worker, 'workDuration').text = str(workers[i].timeOfMining)# + "ms"
    # Lorrys
    vehicleElement = ET.SubElement(root, 'Vehicles')
    for i in range(len(lorrys)):
        lorry = ET.SubElement(vehicleElement, 'Vehicle', {'id': str(i)})
        ET.SubElement(lorry, 'loadTime').text = str(lorrys[i].timeOfFilling)
        transportTime = differenceOfTimeStampsMS(lorrys[i].StartTransportTimeStamp, lorrys[i].EndTransportTimeStamp)
        ET.SubElement(lorry, 'transportTime').text = str(int(transportTime))

    #Building a xml
    dom = xml.dom.minidom.parseString(ET.tostring(root))
    return dom.toprettyxml()

if __name__ == '__main__':
    print('Argument List:', str(sys.argv))

    data = readFile(sys.argv[2])
    parsedData = parseData(data)

    roles = idetifyRoles(parsedData)

    workers, lorrys, farmers, ferrys = createInstances(roles)

    assignRowsToStaff(parsedData, workers, lorrys, farmers, ferrys)

    xmlData = generateXML(parsedData, workers, lorrys, farmers, ferrys)

    writeFile(sys.argv[4], xmlData)