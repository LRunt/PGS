from Role import Role

class Ferry(Role):
    """
    Class represents Ferry
    """

    def __init__(self, name):
        Role.__init__(self, name)
        self.waitTime = 0

    def processData(self, description):
        words = description.split(" ")
        time = int(words[len(words) - 1][:-2])
        self.waitTime = time