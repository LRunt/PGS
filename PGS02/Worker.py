from Role import Role


class Worker(Role):

    def __init__(self, name):
        Role.__init__(self, name)
        self.numberOfMinedBlocks = 0
        self.numberOfMinedSources = 0
        self.timeOfMining = 0