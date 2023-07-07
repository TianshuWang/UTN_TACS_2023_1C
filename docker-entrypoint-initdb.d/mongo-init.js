db = db.getSiblingDB('TP');
db.createUser(
    {
        user: "tacs",
        pwd: "tacs",
        roles: [
            {
                role: "readWrite",
                db: "TP"
            }
        ]
    }
);
