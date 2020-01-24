import React from 'react';
import { Grid, Page} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import details from "./Workshop.json";

class Workshop extends React.Component {
    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Grid.Row>
                        <img
                            alt={details.title + " Cover"}
                            src={details.imageURL}
                        />
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default Workshop;
