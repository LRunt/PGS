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
        self.timeOfDriving = 0
