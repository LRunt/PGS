from Role import Role

class Ferry(Role):
    """
    Class represents Ferry that transport trucks to other side of the river
    Attributes
    waitTime - Time of waiting for 4 trucks
    """

    def __init__(self, name):
        Role.__init__(self, name)
        self.waitTime = 0

    def processData(self, description):
        """
        Method precessing data and saves it to attributes
        if is action important than save value to attribute, else method saves nothing
        :param description: description of action
        """
        words = description.split(" ")
        time = int(words[len(words) - 1][:-2])
        self.waitTime = time