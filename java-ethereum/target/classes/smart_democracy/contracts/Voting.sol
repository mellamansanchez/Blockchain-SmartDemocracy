pragma solidity >=0.4.21 <0.6.0;

contract Voting {
    event AddedCandidate(uint candidateID);
    event AddedId(bytes32 id, address addressUser);

    address owner;
    constructor()public {
        numCandidates = 0;
        numVoters = 0;
        numRegistered = 0;

        owner = msg.sender;
    }
    modifier onlyOwner {
        require(msg.sender == owner, "Sender equals owner");
        _;
    }
    struct Voter {
        bytes32 uid; // bytes32 type are basically strings
        uint candidateIDVote;
    }

    struct Candidate {
        bytes32 name;
        bytes32 party; 
        // "bool doesExist" is to check if this Struct exists
        // This is so we can keep track of the candidates 
        bool doesExist; 
    }
     
    uint numCandidates; 
    uint numVoters;
    uint numRegistered;

    mapping (uint => Candidate) candidates;
    //Mapping of the votes
    mapping (uint => Voter) voters;
    mapping (address => bytes32) addressIdMap;
    address[] usersThatVoted;
    address[] addressUsers;
    uint status = 4;


    function getSenderAddress() public view returns (address) {
        return msg.sender;
    }

    function getStatus() public view returns (uint) {
        return status;
    }

    function getId() public view returns (bytes32) {
        return addressIdMap[msg.sender];
    }

    function logIn(bytes32 idU) public view returns (bool) {
        return addressIdMap[msg.sender] == idU;
    }

    function doesIdUserExist(bytes32 idU) public view returns (bool) {
        for (uint i = 0; i < numRegistered; i++) {
            if(idU == addressIdMap[addressUsers[i]]) {
                return true;
            }
        }
        return false;
    }

    function getCandidateId(bytes32 party, bytes32 candidateName) public view returns (int) {
        for (uint i = 0; i < numCandidates; i++) {
            if(party == candidates[i].party && candidateName == candidates[i].name) {
                return int(i);
            }
        }
        return -1;
    }

    function doesSenderExist() public view returns (bool) {
        return doesAddressExist(msg.sender);
    }

    function doesAddressExist(address addU) public view returns (bool) {
        for (uint i = 0; i < addressUsers.length; i++) {
            if(addU == addressUsers[i]) {
                return true;
            }
        }
        return false;
    }

    function hasUserVoted() public view returns (bool) {
        for (uint i = 0; i < usersThatVoted.length; i++) {
            if(msg.sender == usersThatVoted[i]) {
                return true;
            }
        }
        return false;
    }

    function addNewId(bytes32 idU) public returns (bool) {
        if(doesAddressExist(msg.sender) == true) {
            status = 1;
            return false;
        }
        else if(doesIdUserExist(idU) == true) {
            status = 2;
            return false;
        }
        else {
            addressUsers.push(msg.sender);
            addressIdMap[msg.sender] = idU;
            numRegistered++;
            status = 0;
            emit AddedId(idU, msg.sender);
        }  
    }

    function addCandidate(bytes32 name, bytes32 party) public onlyOwner {
        // candidateID is the return variable
        uint candidateID = numCandidates++;
        // Create new Candidate Struct with name and saves it to storage.
        candidates[candidateID] = Candidate(name,party,true);
        emit AddedCandidate(candidateID);
    }

    function vote(bytes32 uid, uint candidateID) public {
        // users can only vote once
        if(hasUserVoted() == false) {
            // checks if the struct exists for that candidate
            if (candidates[candidateID].doesExist == true) {
                uint voterID = numVoters++; //voterID is the return variable
                voters[voterID] = Voter(uid,candidateID);
                usersThatVoted.push(msg.sender);
            }
        } 
    }

    
    function totalVotes(uint candidateID) public view returns (uint) {
        uint numOfVotes = 0; // we will return this
        for (uint i = 0; i < numVoters; i++) {
            if (voters[i].candidateIDVote == candidateID) {
                numOfVotes++;
            }
        }
        return numOfVotes;
    }

    function getNumOfCandidates() public view returns(uint) {
        return numCandidates;
    }

    function getNumOfVoters() public view returns(uint) {
        return numVoters;
    }

    function getCandidate(uint candidateID) public view returns (uint,bytes32, bytes32) {
        return (candidateID,candidates[candidateID].name,candidates[candidateID].party);
    }
}