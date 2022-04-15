from Role import Role

class Farmer(Role):
    """
    Class represents farmer who manages other roles
    Attributes
    Number of blocks - count of blocks in mine before mining
    Number of sources - count of sources in mine before mining
    """

    def __init__(self, name):
        Role.__init__(self, name)
        self.numberOfBlocks = 0
        self.numberOfSources = 0