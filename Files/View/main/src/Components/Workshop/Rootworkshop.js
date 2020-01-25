import Workshop from "./Workshop/Workshop";
import StudentWorkshop from "./StudentWorkshop/StudentWorkshop";
import TeacherWorkshop from "./TeacherWorkshop/TeacherWorkshop";
import React from "react";
import enrolledWorkshops from "../Dashboard/EnrolledWorkshops";
import {Button, Card, Table} from "tabler-react";
import axios from "axios";


class Rootworkshop extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            teacher: {},
            workshop: {},
            role: "",
            startTime: "",
            finishTime: ""
        }
    }


    async componentWillMount(): void {
        const {workshopID} = this.props.match.params;
        const toSend = {workshopId:workshopID};
        console.log(toSend);
        await axios.post("http://localhost:8000/user/workshop/page", toSend).then(res => {

            this.setState({teacher: res.data.body.teacher});
            this.setState({workshop: res.data.body.workshop,startTime:res.data.body.workshop.startTime,finishTime:res.data.body.workshop.startTime});
            this.setState({role: res.data.body.role});
        }).catch(e => {
            console.log(e)
        })
    }

    render() {
        if (this.state.role == null) {
            return <Workshop/>
        }
        else if (this.state.role.roleName === "Student") {
            return <StudentWorkshop/>
        }
        else if (this.state.role.roleName === "Teacher") {
            return <TeacherWorkshop/>
        }
    }
}

export default Rootworkshop;

