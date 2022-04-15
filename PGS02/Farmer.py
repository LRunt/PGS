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

    def processData(self, description):
        """
        Method precessing data and saves it to attributes
        :param description: description of action
        :return: if is action important than save value to attribute, method returns nothing
        """
        print(description)
        words = description.split(" ")
        if description.__contains__("blocks"):
            self.numberOfBlocks = int(words[len(words) - 1])
        elif description.__contains__("sources"):
            self.numberOfSources = int(words[len(words) - 1])