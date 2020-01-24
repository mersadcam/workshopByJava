import React from 'react';
import {Card, Grid, Page} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import details from "./details.json";


class Workshop extends React.Component {
    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Grid.Row>
                        <Grid.Col lg={5}>
                            <img
                                alt={details.title + " Cover"}
                                src={details.imageURL}/>
                        </Grid.Col>
                        <Grid.Col>
                            <Card>
                                <Card.Body>
                                    <h3 className={'d-inline'}>  </h3>
                                </Card.Body>
                            </Card>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default Workshop;
