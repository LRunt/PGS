from Role import Role


class Lorry(Role):

    def __init__(self, name):
        Role.__init__(self, name)
        self.timeOfFilling = 0
        self.timeOfDriving = 0
