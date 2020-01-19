import React from 'react';
import {Grid, Page} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import MyWorkshops from "./MyWorkshops";
import Summary from "./Summary";
import Messages from "./Messages";

class Dashboard extends React.Component {
    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Grid.Row>
                        <Grid.Col sm={'12'} lg={'6'}>
                            <Summary/>
                            <MyWorkshops/>
                        </Grid.Col>
                        <Grid.Col sm={'12'} lg={'6'}>
                            <Messages/>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default Dashboard;
