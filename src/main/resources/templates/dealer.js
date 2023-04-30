const creatingForm = document.getElementById('creating');
creatingForm.addEventListener('submit', submitCreateDealerHandler);
const card = document.getElementById('card');
const button = document.getElementById('eventBtn');
const purchasingForm = document.getElementById('purchasing-form');
const townForm = document.getElementById('town-form');
const eventBtn = document.getElementById('eventBtn');
const cardImg = document.getElementById('cardImg');
const cardTitle = document.getElementById('cardTitle');
const cardDescription = document.getElementById('cardDescription');

infoHandler();
async function infoHandler(){
    const currentInfo = await axios.get('http://127.0.0.1:8089/dealers/info');
    if(currentInfo.data != null){
        console.log(currentInfo.data);
        cardImg.src = currentInfo.data.imageName;
        cardTitle.innerText = currentInfo.data.title;
        cardDescription.innerText = currentInfo.data.description;
        if(currentInfo.data.title !== 'Bazaar') {
            purchasingForm.style.display = 'none';
            townForm.style.display = 'none';
            button.style.display = 'unset';
        }
    }
}

async function submitCreateDealerHandler(e){
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const json = parseJson(data);
    const response = await axios.post('http://127.0.0.1:8089/dealers/create', json,
        {
            headers: {
                'Content-Type':'application/json',
            }
        });
    console.log(response);
    creatingForm.style.display = 'none';
    card.style.position = 'relative';
}

function parseJson(data){
    let object = {};
    data.forEach(function(value, key){
        object[key] = value;
    });
    return JSON.stringify(object);
}

button.addEventListener('click', eventHandler);

async function eventHandler(e){
    e.preventDefault();
    const response = await axios.post('http://127.0.0.1:8089/dealers/way',
        {
            headers: {
                'Content-Type':'application/json',
            }
        });
    cardImg.src = response.data.imageName;
    cardTitle.innerText = response.data.title;
    cardDescription.innerText = response.data.description;
    if(response.data.imageName === '../static/start.jpg'){
        purchasingForm.style.display = 'block';
        document.getElementById('purchaseBtn').style.display = 'block';
        townForm.style.display = 'block';
        eventBtn.style.display = 'none';
    } else {
        purchasingForm.style.display = 'none';
        document.getElementById('purchaseBtn').style.display = 'none';
    }
}

townForm.addEventListener('submit', townFormHandler);

async function townFormHandler(e){
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const response = await axios.get('http://127.0.0.1:8089/dealers/towns',
        {
            params: { townName: data.get('townName')}
        })
    console.log(response);
    townForm.style.display = 'none';
    eventBtn.style.display = 'block';
}

purchasingForm.addEventListener('submit', purchasingHandler);
async function purchasingHandler(e){
    e.preventDefault();
    const form = e.target;
    const data = new FormData(form);
    const response = await axios.get('http://127.0.0.1:8089/dealers/products/purchase',
        {
            params: { productName: data.get('productName')}
        });
    console.log(response);
}


