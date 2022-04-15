from Role import Role


class Ferry(Role):

    def __init__(self, name):
        Role.__init__(self, name)
        self.numberOfTransports = 0