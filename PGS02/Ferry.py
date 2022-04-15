from Role import Role

class Ferry(Role):
    """
    Class represents Ferry
    """

    def __init__(self, name):
        Role.__init__(self, name)
        self.numberOfTransports = 0