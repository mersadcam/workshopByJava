import React from 'react';
import {Card, Grid, Page, Form, Button, Stamp} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import axios from "axios"

class ViewForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            form: {},
            name:"",
            workshopId:""
        }
    }

    componentDidMount(): void {

        const {formID,workshopID} = this.props.match.params
        this.setState({workshopId:workshopID})
        axios.post("http://localhost:8000/user/form",{formId:formID}).then(res=>{

            this.setState({form:res.data.formBody,name:res.data.name})

        }).catch(e=>{
            console.log(e)
        })

    }

    render() {

        const {form,workshopId} = this.state

        return (
            <SiteTemplate>
                <Page.Content>
                    {Object.keys(form).map((item) => (
                        <Card>
                            <Card.Status color="blue" side />
                            <Card.Header>
                                <Card.Title>
                                    {parseInt(item)+1} : {form[item]}
                                </Card.Title>
                            </Card.Header>
                        </Card>
                    ))}
                    <Grid.Row className={'text-center'}>
                        <Grid.Col>
                            <Button color='blue' RootComponent={'a'} href={"/rootworkshop/"+workshopId}>Go Back</Button>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default ViewForm;
