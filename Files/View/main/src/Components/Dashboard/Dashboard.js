import React from 'react';
import {Grid, Page} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import MyWorkshops from "./MyWorkshops";
import Messages from "./Messages";
import NewWorkshops from "./NewWorkshops";
import axios from "axios"

class Dashboard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {

            newWorkshops: [],
            myWorkshops: [],
            messages: []

        }
    }

    async componentDidMount(): void {

        await axios.get("http://localhost:8000/user/dashboard").then(res=>{
            this.setState({myWorkshops:res.data.body.workshops,newWorkshops:res.data.body.newWorkshops,messages:res.data.body.messages})

        }).catch(e=>{

            console.log(e)

        })

    }

    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Grid.Row>
                        <NewWorkshops workshops={this.state.newWorkshops}/>
                    </Grid.Row>
                    <Grid.Row>
                        <Grid.Col sm={'12'} lg={'6'}>
                            <MyWorkshops workshops={this.state.myWorkshops}/>
                        </Grid.Col>
                        <Grid.Col sm={'12'} lg={'6'}>
                            <Messages messages={this.state.messages}/>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default Dashboard;
