from Role import Role


class Worker(Role):
    """
    Class represents worker who work in mine and mining a blocks
    Attributes:
    numberOfMinedBlocks - count of blocks what worker has already mined
    numberOfMinedSources - count of sources what worker has already mined
    timeOfMining - time what worker has already worked
    """

    def __init__(self, name):
        Role.__init__(self, name)
        self.numberOfMinedBlocks = 0
        self.numberOfMinedSources = 0
        self.timeOfMining = 0