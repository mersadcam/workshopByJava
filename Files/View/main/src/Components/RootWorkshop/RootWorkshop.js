import React from 'react';
import axios from "axios"
import Workshop from "../Workshop/Workshop";
import StudentWorkshop from "../StudentWorkshop/StudentWorkshop";
import TeacherWorkshop from "../TeacherWorkshop/TeacherWorkshop";
import GraderWorkshop from "../GraderWorkshop/GraderWorkshop";

class RootWorkshop extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            teacher: {},
            workshop: {},
            role: {},
            startTime:"",
            finishTime:""
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
        return (
            <div>
                {this.state.role.roleName === "Student" &&
                <StudentWorkshop
                    teacher={this.state.teacher}
                    workshop={this.state.workshop}
                    role={this.state.role}
                    startTime={this.state.startTime}
                    finishTime={this.state.finishTime}

                />}


                {this.state.role.roleName === "Teacher" &&
                <TeacherWorkshop
                    teacher={this.state.teacher}
                    workshop={this.state.workshop}
                    role={this.state.role}
                    startTime={this.state.startTime}
                    finishTime={this.state.finishTime}

                />}

                {this.state.role.roleName === "Grader" &&
                <GraderWorkshop
                    teacher={this.state.teacher}
                    workshop={this.state.workshop}
                    role={this.state.role}
                    startTime={this.state.startTime}
                    finishTime={this.state.finishTime}

                />}

                {this.state.role === "" &&
                <Workshop
                    teacher={this.state.teacher}
                    workshop={this.state.workshop}
                    role={this.state.role}
                    startTime={this.state.startTime}
                    finishTime={this.state.finishTime}
                />}
            </div>
        )
    }
}

export default RootWorkshop;
