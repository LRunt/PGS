from Role import Role


class Farmer(Role):

    def __init__(self, name):
        Role.__init__(self, name)
        self.numberOfBlocks = 0
        self.numberOfSouces = 0