import styled from 'styled-components';
import PostBox from '../components/PostBox';
import { BiCaretDownCircle } from 'react-icons/bi';
import { useParams } from 'react-router-dom';
import CATEGORY_CODE from '../constants/index';
import { useState, useEffect, useMemo } from 'react';
import axios from 'axios';
const PRODUCT = {
  outer: '아우터',
  top: '상의',
  bottom: '하의',
  onepiece: '원피스',
  hat: '모자',
  shoes: '신발',
};

const Category = () => {
  const params = useParams();
  const category = params.categoryId;
  const [data, setData] = useState([]);
  const [check, setCheck] = useState(false);
  console.log(data);
  const onDpMenu = () => {
    let click = document.getElementById('drop-content');
    if (click.style.display === 'none') {
      click.style.display = 'flex';
    } else {
      click.style.display = 'none';
    }
  };

  const fetchData = async () => {
    try {
      const response = await axios.get(`http://13.125.30.88/boards`);
      setData(response.data.data);
    } catch {
      window.alert('오류가 발생했습니다.');
    }
  };
  const fetchRentData = async () => {
    try {
      const response = await axios.get(
        `http://13.125.30.88/boards/search/available`
      );
      setData(response.data.data);
      console.log(response.data.data);
    } catch {
      window.alert('오류가 발생했습니다.');
    }
  };
  //렌탈 가능한 상품만 받아서 true일떄는 그 데이터로
  useEffect(() => {
    window.scrollTo(0, 0);
    if (check === true) {
      fetchRentData();
    } else if (check === false) {
      fetchData();
    }
  }, [check]);
  console.log(check);
  const currentCategoryProducts = useMemo(() => {
    return data.filter((item) => {
      return item?.products.some(
        (product) => product.category === PRODUCT[category]
      );
    });
  }, [data, category]); // 종속성으로 category 를 넣고,category가 변경되면 이 변수값이 업데이트
  console.log(currentCategoryProducts);
  //렌탈가능
  const onCheck = () => {
    const checkbox = document.getElementById('checkbox');
    if (checkbox.checked === true) {
      setCheck(true);
    } else {
      setCheck(false);
    }
  };
  //정렬
  //최신순
  const onNew = () => {
    let newArr = [...currentCategoryProducts];
    let newestResult = newArr.sort((a, b) => {
      return b.boardId - a.boardId;
    });
    setData(newestResult);
  };
  //가격 높은순
  const onCheap = () => {
    let newArr = [...currentCategoryProducts];
    let newestResult = newArr.sort((a, b) => {
      return a.products[0].price - b.products[0].price;
    });
    setData(newestResult);
  };
  const onExpensive = () => {
    let newArr = [...currentCategoryProducts];
    let newestResult = newArr.sort((a, b) => {
      return b.products[0].price - a.products[0].price;
    });
    setData(newestResult);
  };
  return (
    <SWrapper>
      <div className="category">
        <div className="main post">
          <span className="category-name">{CATEGORY_CODE[category]}</span>
          <Filter>
            <div className="rental availability ">
              <input
                type="checkbox"
                name="rental"
                value=""
                id="checkbox"
                onClick={onCheck}
              />
              렌탈 가능
            </div>
            <div className="dropdown">
              <button className="dropdown_button" onClick={onDpMenu}>
                <BiCaretDownCircle />
                정렬 순서
              </button>
              <div id="drop-content">
                <button
                  onClick={() => {
                    onCheap();
                    onDpMenu();
                  }}
                >
                  가격 낮은순
                </button>
                <button
                  onClick={() => {
                    onExpensive();
                    onDpMenu();
                  }}
                >
                  가격 높은순
                </button>
                <button
                  onClick={() => {
                    onNew();
                    onDpMenu();
                  }}
                >
                  최신순
                </button>
              </div>
            </div>
          </Filter>
          <PostBox data={currentCategoryProducts} />
        </div>
      </div>
    </SWrapper>
  );
};
const SWrapper = styled.div`
  display: flex;
  justify-content: flex-end;
  .category {
    width: 73%;
    display: flex;
    justify-content: flex-start;
    left: 0;
    right: 0;
    top: 0;
  }
  .post {
    width: 80%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
  .category-name {
    text-align: center;
    font-size: 50px;
    margin-top: 50px;
  }
`;
const Filter = styled.div`
  display: flex;
  width: 98%;
  justify-content: space-between;
  .availability {
    margin-top: 50px;
    margin-bottom: 10px;
  }
  .dropdown_button {
    margin-top: 40px;
    font-size: 20px;
    width: 120px;
    height: 35px;
    position: releative;
    border: none;
    display: flex;
    align-items: center;
    cursor: pointer;
    background-color: white;
  }
  #drop-content {
    display: flex;
    display: none;
    width: 120px;
    flex-direction: column;
    position: absolute;
    z-index: 999;
    button {
      height: 30px;
      border: 0.1px solid gray;
      cursor: pointer;
      background-color: white;
    }
  }
`;
export default Category;
