import datetime

from Role import Role


class Lorry(Role):
    """
    Class represents lorry(truck) which transport mined blocks to other side of river
    Attributes:
    timeOfFilling - total time of filling of the truck
    timeOfDriving - total time of transporting block from point A to point B
    """

    def __init__(self, name):
        Role.__init__(self, name)
        self.timeOfFilling = 0
        self.StartTransportTimeStamp = datetime.datetime.now()
        self.EndTransportTimeStamp = datetime.datetime.now()

    def processData(self, description, timeStamp):
        words = description.split(" ")
        if description.__contains__("fill"):
            time = int(words[len(words) - 1][:-2])
            self.timeOfFilling = time
            self.StartTransportTimeStamp = timeStamp
        if description.__contains__("arrived"):
            self.EndTransportTimeStamp = timeStamp
